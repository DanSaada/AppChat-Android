const androidTokens = require('../androidTokens');
const userService = require('../services/user');
const socketsManager = require('../socketManager');

const createUser = async (req, res) => {
    const { username, password, displayName, profilePic } = req.body;
    res.send(await userService.createUser(username, password, displayName, profilePic));
};

const getUsers = async (req, res) => {
    res.send(await userService.getUsers());
};

const getUser = async (req, res) => {
    if(req.headers.authorization){
        // extract token from header "Bearer + token"
        const token = req.headers.authorization.split(' ')[1];
        // check validation of the token and extract the data from it
        const data = userService.checkToken(token);
        try{
            if(data){
                //find the specific chat by its id
                const user = await userService.getUserByUsername(req.params.username);
                if(!user){
                    return res.status(404).json({ errors: ['User not found'] });
                }
                return res.send(user);
            }
        }catch (error) {
            return res.status(401).json({ errors: ['Unauthorized', error.message] });
        }
    }else{
        return res.status(401).json({ errors: ['Unauthorization'] });
    }
};

const generateToken = async  (req, res) => {
    const {username, password} = req.body;

    // if the header contains additional field that represent the req came from an android app
    if(req.headers['androidtoken']) {
        for(var i = 0; i < androidTokens.length; i++) {
            //check if the token is already present in the androidTokens array
            if (androidTokens[i]['androidtoken'] === req.headers['androidtoken']) {
                delete androidTokens[i];
                break;
            }
        }
        androidTokens[req.body.username] = req.headers['androidtoken'];
        delete socketsManager[req.body.username];
    }else {
        if (androidTokens[req.body.username]) {
            delete androidTokens[req.body.username];
        }
    }

    const token = await userService.generateToken(username, password);
    if(!token) {
        return res.status(404).json({ errors: ['Incorrect username and/or password'] });
    }
    return res.send(token);
}

module.exports = { createUser, getUsers, getUser, generateToken }