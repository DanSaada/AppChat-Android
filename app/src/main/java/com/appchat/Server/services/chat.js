const Chat = require('../models/chat');
const User = require('../models/user');
const Message = require('../models/message');

const createChat = async (username, data) => {
    // define a new id for the chat as the max chat id + 1
    const maxChatID = await Chat.findOne().sort('-id').limit(1).exec();
    let chatID = 1;
    // check for existing of chats
    if (maxChatID && maxChatID.id) {
        chatID = maxChatID.id + 1;
    }
    
    // find the users in the db by their names
    const currentUser = await User.findOne({ "username" : data });
    const foundUser = await User.findOne({ "username" : username });

    if (!currentUser) { return false; }
    if (!foundUser) { return false; }
    
    const users = [currentUser, foundUser];

    const chat = new Chat({ id: chatID, users: users, messages: [] });
    
    //because we await, the return wont return a promise,
    //but it will return a Chat object from the db (with an accsess to the chat id).
    const newChat = await chat.save();

    if (newChat) {
        return {
            "id": newChat.id,
            "user": {
                "username": foundUser.username,
                "displayName": foundUser.displayName,
                "profilePic": foundUser.profilePic

            }
        };
    } else { 
        
        return false; }
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
  
  const formatMessage = (message) => {
    if (!message) {
      return null; // Return null if message is not found
    }
  
    const formatedMessageId = message.id;
    const formatedMessageCreated = message.created;
    const formatedMessageContent = message.content;
  
    const formatedMessageToReturn = {
      id: formatedMessageId,
      created: formatedMessageCreated,
      content: formatedMessageContent,
    };
  
    return formatedMessageToReturn;
  };
  

const getChats = async (username) => {

    try {
        var data = [];
        const foundUser2 = await User.findOne({ username: username });
       
        const chats = await Chat.find({ users: { $in: [foundUser2._id] } });

        for (let chat of chats) {
            
            // find the users of the current chat in the db
            const user1 = await User.findOne({ _id: chat.users[0] }); 
            const user2 = await User.findOne({ _id: chat.users[1] }); 

            // extract the relevent data from them
            const userFormated1 = formatUser(user1);
            const userFormated2 = formatUser(user2);
            
            if (!userFormated1 || !userFormated2) {
                continue; // Skip this iteration if either user is null
            }

            var lastMsg;
            if (chat.messages.length === 0) {
                lastMsg = null;
            } else {

                lastMsg = chat.messages[chat.messages.length - 1];

                lastMsg = await Message.findById({ _id: lastMsg });

                lastMsg = formatMessage(lastMsg);
            }
            
            var user;
            if (userFormated1.username === username) {
                user = userFormated2;
            } else {
                user = userFormated1;
            }
            const send = { id: chat.id, user: user, lastMessage: lastMsg };
            data = [...data, send];
        }
        return data;
 
    } catch (error) {
        console.log(error)
        return -10;
    }
}

const getChatById = async (id) => {
    return await Chat.findOne({ "id": id });
};

const deleteChat = async (id) => {
    const chat = await getChatById(id);
    if (!chat) { return null; }
    await chat.deleteOne();
    return chat;
};


module.exports = { createChat, getChats, getChatById, deleteChat }