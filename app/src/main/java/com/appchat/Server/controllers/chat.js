const { response } = require('express');
const androidTokens = require('../androidTokens');
const chatService = require('../services/chat');
const userService = require('../services/user');
const socketsManager = require('../socketManager');
const admin = require('firebase-admin');

const createChat = async (req, res) => {
    if(req.headers.authorization){
        // extract token from header "Bearer + token"
        const token = req.headers.authorization.split(' ')[1];
        // check validation of the token and extract the data from it
        const data = userService.checkToken(token);
        try{
            if(data){
                //find the specific chat by its id
                const newChat = await chatService.createChat(req.body.username, data);
                if(!newChat){
                    return res.status(404).json({ errors: ['Chat error create'] });
                }

                // check if the user is connected through Android device
                if (androidTokens[req.body.username]) {
                    // generate the message
                    const message = {
                        notification: {
                            title: 'New chat',
                            body: data + " has initiated a conversation with you."
                        },
                        data: {
                            action: 'addNewContact',
                        },
                        token: androidTokens[req.body.username]
                    };
                    // send the notification using Firebase
                    admin.messaging().send(message)
                        .then((response) => {
                            console.log('Successfully sent message: ', response);
                        })
                        .catch((error) => {
                            console.log('Error sending message: ', error);
                        });
                // check if the receiver has an Android device and a token, but the sender does not have a token
                }else if (androidTokens[data.username] && !androidTokens[req.body.username]) {
                    if (socketsManager[req.body.username]) {
                        await socketsManager[req.body.username].emit('addNewContact')
                    }
                }
                
                return res.send(newChat);
            }
        }catch (error) {
            return res.status(401).json({ errors: ['Unauthorized', error.message] });
        }
    }else{
        return res.status(401).json({ errors: ['Unauthorization'] });
    }
};

const getChats = async (req, res) => {
    if(req.headers.authorization){
        // extract token from header "Bearer + token"
        const token = req.headers.authorization.split(' ')[1];
        // check validation of the token and extract the data from it
        const data = userService.checkToken(token);
        try{
            if(data){
                
                //find the specific chat by its id
                const chats = await chatService.getChats(data);

                if(!chats){
                    return res.status(404).json({ errors: ['Chat not found'] });
                }
               
                return res.send(chats);
            }
        }catch (error) {
            return res.status(401).json({ errors: ['Unauthorized', error.message] });
        }
    }else{
        return res.status(401).json({ errors: ['Unauthorization'] });
    }
};
  

const getChat = async (req, res) => {
    if(req.headers.authorization){
        // extract token from header "Bearer + token"
        const token = req.headers.authorization.split(' ')[1];
        // check validation of the token and extract the data from it
        const data = userService.checkToken(token);
        try{
            if(data){
                //find the specific chat by its id
                const chat = await chatService.getChatById(req.params.id);
                if(!chat){
                    return res.status(404).json({ errors: ['Chat not found'] });
                }
                return res.send(chat);
            }
        }catch (error) {
            return res.status(401).json({ errors: ['Unauthorized', error.message] });
        }
    }else{
        return res.status(401).json({ errors: ['Unauthorization'] });
    }
};

const deleteChat = async (req, res) => {
    if(req.headers.authorization){
        // extract token from header "Bearer + token"
        const token = req.headers.authorization.split(' ')[1];
        // check validation of the token and extract the data from it
        const data = userService.checkToken(token);
        try{
            if(data){
                //delete the chat
                const chat = await chatService.deleteChat(req.params.id);
                if(!chat){
                    return res.status(404).json({ errors: ['Chat not found'] });
                }
                return res.sendStatus(204);
            }
            
        }catch (error) {
            return res.status(401).json({ errors: ['Unauthorized', error.message] });
        }

    }else{
        return res.status(401).json({ errors: ['Unauthorization'] });
    }
}

module.exports = { createChat, getChats, getChat, deleteChat }
