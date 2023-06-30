const User = require('../models/user');
const jwt = require('jsonwebtoken');

const createUser = async (username, password, displayName, profilePic) => {
    const user = new User({ username: username, password: password, displayName: displayName, profilePic: profilePic });

    //because we await, the return wont return a promise,
    //but it will return a User object from the db (with an accsess to the user id).
    return await user.save();
};

const getUsers = async () => { 
    return await User.find({}); 
};

const getUserByUsername = async (username) => {
    return await User.findOne({"username": username});
    // return await User.findByTagName(username);
};


// KEY
const key = 'y6SNjgPbm3X^x2jgX5nG@8dT2T!D9X';

const generateToken = async (username, password) => {
    try {
        // find user by its name
        const user = await User.findOne({ "username" : username, "password" : password }).populate('username password');

        // return the users token
        if(user) {return jwt.sign(user.username, key);}
        return false;

    }catch(error){
        console.error(error);
    }
}

// if the token is ok, the function returns the decoded token payload data
const checkToken = (token) => {
    return jwt.verify(token, key);
}


module.exports = { createUser, getUsers, getUserByUsername, generateToken, checkToken }