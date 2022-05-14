(function () {
    'use strict';

    angular
        .module('app')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['UserService', 'MessageService', 'AuthenticationService', 'FlashService'];

    function HomeController(UserService, MessageService, AuthenticationService, FlashService) {
        var vm = this;

        vm.user = null;
        vm.allUsers = [];

        vm.logout = logout;

        initController();

        function initController() {
            loadCurrentUser();
            initApplication()
        }

        function loadCurrentUser() {
            var currentUser = JSON.parse(localStorage.getItem('currentUser'));
            UserService.GetByUsername(currentUser.username)
                .then(function (user) {
                    vm.user = user;
                });
        }

        function logout() {
            const promise = AuthenticationService.logout();
            promise.then(response => {
                FlashService.Success(response.data.message, true);
                localStorage.removeItem('currentUser');
                document.location.href = "/";
            }).catch(error => FlashService.Error(error));
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

        function updateMessages() {
            $('.messages-list').html('<i>Сообщений нет</i>');
            const promise = MessageService.GetMessages();
            promise.then(response => {
                const messages = response.data;
                console.log(messages)
                if (messages.length === 0) {
                    return;
                }
                $('.messages-list').html('');
                messages.forEach(message => {
                    const element = getMessageElement(message);
                    $('.messages-list').append(element);
                })
            }).catch(error => FlashService.Error(error))
        }

        function initApplication() {
            $('.messages-and-users').css({display: 'flex'});
            $('.controls').css({display: 'flex'});

            $('.send-message').on('click', function () {
                let message = $('.new-message').val();

                const promise = MessageService.AddMessages(message);
                promise.then(() => {
                    $('.new-message').val('');
                }).catch(error => FlashService.Error(error))
            });

            setInterval(updateMessages, 1000);
        }

    }

})();