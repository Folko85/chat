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
            initApplication()
        }

        function loadCurrentUser() {
            const currentUser = JSON.parse(localStorage.getItem('currentUser'));
            UserService.GetByUsername(currentUser.username)
                .then(user => {
                    vm.user = user;
                });
            SocketService.connect(currentUser)
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