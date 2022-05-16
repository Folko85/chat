(function () {
    'use strict';

    angular
        .module('app')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['UserService', 'MessageService', 'SocketService', 'AuthenticationService', 'FlashService'];

    function HomeController(UserService, MessageService, SocketService, AuthenticationService, FlashService) {
        var vm = this;

        vm.user = null;
        vm.allUsers = [];

        vm.logout = logout;


        initController();

        function initController() {
            loadCurrentUser();
            initApplication();
            loadLastMessages();
        }

        function loadCurrentUser() {
            const currentUser = JSON.parse(localStorage.getItem('currentUser'));
            UserService.GetByUsername(currentUser.username)
                .then(user => {
                    vm.user = user;
                });
            SocketService.connect(currentUser);
        }

        function loadLastMessages() {
            const promise = MessageService.GetMessages();
            promise.then(response => {
                response.data.forEach(message => {
                    addMessage(message);
                })
            }).catch(error => {
                FlashService.Error(error);
            })
        }

        function getMessageElement(message) {
            let item = $('<div class="message-item"></div>');
            let header = $('<div class="message-header"></div>');
            header.append($('<div class="datetime">' +
                message.dateTime + '</div>'));
            header.append($('<div class="username">' +
                message.username + '</div>'));
            let textElement = $('<div class="message-text"></div>');
            textElement.text(message.text);
            item.append(header, textElement);
            return item;
        }

        function addMessage(message) {
            if (message.text) {
                const element = getMessageElement(message);
                $('.messages-list').append(element);
                const block = document.getElementById("messages");
                block.scrollTop = block.scrollHeight;
            }
        }

        function logout() {
            const promise = AuthenticationService.logout();
            promise.then(response => {
                FlashService.Success(response.data.message, true);
                localStorage.removeItem('currentUser');
                document.location.href = "/";
            }).catch(error => FlashService.Error(error));
        }

        function initApplication() {
            $('.messages-and-users').css({display: 'flex'});
            $('.controls').css({display: 'flex'});

            $('.new-message').keypress(function (e) {
                const key = e.which;
                if (key === 13)  // the enter key code
                {
                    $('.send-message').click();
                    return false;
                }
            });

            $('.send-message').on('click', function () {
                let message = $('.new-message').val();
                SocketService.sendMessage(message)
            });

        }

    }

})();