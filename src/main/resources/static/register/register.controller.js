(function () {
    'use strict';

    angular
        .module('app')
        .controller('RegisterController', RegisterController);

    RegisterController.$inject = ['UserService', '$location', '$rootScope', 'FlashService'];

    function RegisterController(UserService, $location, $rootScope, FlashService) {
        var vm = this;

        vm.register = register;

        function register() {
            vm.dataLoading = true;
            const promise = UserService.Create(vm.user);
            promise.then(response => {
                FlashService.Success(response.data.message, true);
                $location.path('/login');
            }).catch(error => {
                FlashService.Error(error.data.error_description);
                vm.dataLoading = false;
            })
        }
    }

})();
