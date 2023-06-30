const Message = require('../models/message');
const Chat = require('../models/chat');
const User = require('../models/user');
const mongoose = require('mongoose');

const sendMessage = async (msg, data, chatId) => {
    // define a new id for a message as the max message id + 1
    const maxMessageId = await Message.findOne().sort('-id').limit(1).exec();
    let messageId = 1;
    // check for existing of messagess and generate a message id
    if (maxMessageId && maxMessageId.id) {
        messageId = maxMessageId.id + 1;
    }
    //find the user who sent the message
    const foundUser = await User.findOne({"username": data});

    //create new messsage
    const message = new Message({ id: messageId, created: new Date(), sender: foundUser, content: msg });

    // find the chat and add the message to it
    const chat = await Chat.findOne({ id: chatId });
    if (chat) {
        chat.messages.push(message);
        await chat.save();
    }

    const newMessage = await message.save();

    if(newMessage){
        return newMessage;
    }else{
        return false;
    }
};

const formatUser = (user) => {
    if (!user) {
      return null; // Return null if user is not found
    }
    
    const formatedUsername = user.username;
    
    const formatedDisplayName = user.displayName;
    const formatedProfilePic = user.profilePic;
  
    const formatedUserToReturn = {
      username: formatedUsername,
      displayName: formatedDisplayName,
      profilePic: formatedProfilePic,
    };
  
    return formatedUserToReturn;
  };

const getMessages = async (id) => {
    try {
        var data = [];
        
         const chat = await Chat.findOne({"id" : id}).populate("messages");
         
        
        for (let message of chat.messages) {
            // find the users of the current chat in the db
            const user1 = await User.findOne({ _id: message.sender }); 
          
            // extract the relevent data from them
            const userFormated1 = formatUser(user1);
            

            const send = { id: message.id, created: message.created, sender: { username: userFormated1.username }, content: message.content };
            data = [...data, send];
        }

        

        if (data) {
            return data;
        } else {
            return null;
        }
    } catch (error) {
        throw error;
    }
};


module.exports = { sendMessage, getMessages }
    