const userController = require('../controllers/user');

const express = require('express');
var router = express.Router();

router.route('/')
    .get(userController.getUsers)
    .post(userController.createUser);

router.route('')

router.route('/:username')
    .get(userController.getUser)

module.exports = router;