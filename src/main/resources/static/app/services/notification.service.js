(function () {
    angular.module(window.angularModuleName)
        .factory('NotificationService', NotificationService);

    NotificationService.$inject = ['$log'/*, 'webNotification'*/];

    function NotificationService($log/*, webNotification*/) {
        return {
            notify: function (notification) {
                $log.debug('Notification shown: ', notification);
                /*webNotification.showNotification(notification.title || 'CCC', {
                    body: notification.body,
                    icon: '/img/acct-logo-32.png'
                }, function onShow(error, hide) {
                    if (error) {
                        return $log.debug('Unable to show notification', error);
                    }
                    $log.debug('Notification shown');
                });*/
            }
        };
    }
}());