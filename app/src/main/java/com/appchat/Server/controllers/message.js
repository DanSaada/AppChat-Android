const androidTokens = require('../androidTokens');
const messageService = require('../services/message');
const userService = require('../services/user');
const socketsManager = require('../socketManager');
const Chat = require('../models/chat');
const User = require('../models/user');
const admin = require('firebase-admin');

const sendMessage = async (req, res) => {
    if(req.headers.authorization){
        // extract token from header "Bearer + token"
        const token = req.headers.authorization.split(' ')[1];
        // check validation of the token and extract the data from it
        const data = userService.checkToken(token);
        try{
            if(data){
                // Extract the chat ID from the route parameter
                const chatId = req.params.id;
                // extract the message from the body
                const msg = req.body.msg;
                //find the specific chat by its id
                const newMessage = await messageService.sendMessage(msg, data, chatId);
                if(!newMessage){
                    return res.status(404).json({ errors: ['Message error sending'] });
                }

                //extract the contact that receives the message
                let talkingTo;
                const chat = await Chat.findOne({ id: chatId }).populate('users');

                // find the users of the current chat in the db
                const foundUser1 = await User.findOne({ _id: chat.users[0] }); 
                const foundUser2 = await User.findOne({ _id: chat.users[1] }); 
                
                if (foundUser1.username === data) {
                    talkingTo = foundUser2.username;
                }

                // check if the contact has an associated Android token
                if (androidTokens[talkingTo]) {
                    //ensure that the message fits within the push notification limits.
                    for(let i=0; i < msg.length; i+=3000) {
                        const message = {
                            notification: {
                                title: 'New message from ' + data,
                                body: newMessage.content.substring(i, i+3000),
                            },
                            data: {
                                action: 'sendNewMessage',
                                senderUsername: foundUser1.username,
                                senderDisplayName: foundUser1.displayName,
                                receiver: talkingTo,
                                date: newMessage.created.toISOString().toString(),
                                msgID: newMessage.id.toString(),
                                chatID: chatId.toString(),
                            },
                            token: androidTokens[talkingTo]
                        };
                        // send the notification using Firebase
                        await admin.messaging().send(message)
                            .then((response) => {
                                console.log('Successfully sent message: ', response);
                            })
                            .catch((error) => {
                                console.log('Error sending message: ', error);
                            });
                    }
                // check if the sender has an associated Android token, but the receiver does not
                }
                else if (androidTokens[data] && !androidTokens[talkingTo]) {
                    var newMsg = {
                        chatID: chatId,
                        message: {
                            message: msg,
                            pic: {[data]: newMessage.sender.profilePic},
                            me: data,
                            date: newMessage.created
                        },
                        receiverUsername: talkingTo
                    }
                    if (socketsManager[talkingTo]) {
                        await socketsManager[talkingTo].emit("receiveMessage", newMsg);
                    }        
                }

                return res.send(newMessage);
            }
        }catch (error) {
            return res.status(401).json({ errors: ['Unauthorized', error.message] });
        }
    }else{
        return res.status(401).json({ errors: ['Unauthorization'] });
    }
};

const getMessages = async (req, res) => {
    if(req.headers.authorization){
        // extract token from header "Bearer + token"
        const token = req.headers.authorization.split(' ')[1];
        // check validation of the token and extract the data from it
        const data = userService.checkToken(token);
        try{
            if(data){
                //find the specific chat by its id
                const messages = await messageService.getMessages(req.params.id);
                if(!messages){
                    return res.status(404).json({ errors: ['Messages not found'] });
                }
                return res.send(messages);
            }
        }catch (error) {
            return res.status(401).json({ errors: ['Unauthorized', error.message] });
        }
    }else{
        return res.status(401).json({ errors: ['Unauthorization'] });
    }
};

module.exports = { sendMessage, getMessages }