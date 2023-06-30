const chatController = require('../controllers/chat');
const messageController = require('../controllers/message');

const express = require('express');
var router = express.Router();

router.route('/')
    .get(chatController.getChats)
    .post(chatController.createChat);

router.route('/:id')
    .get(chatController.getChat)
    .delete(chatController.deleteChat);

router.route('/:id/Messages')
    .get(messageController.getMessages)
    .post(messageController.sendMessage)

module.exports = router;
