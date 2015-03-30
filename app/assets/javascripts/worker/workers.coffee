workersModule = angular.module 'WorkersModule', []
ASSETS_FOLDER = "assets"

workersModule.factory 'workersController', ($http, $log) ->

  WORKERS_WS_URL = "http://localhost:9000/workers/"

  self = @
  @workers = []

  @fetchWorkers = (userId) ->
    url = WORKERS_WS_URL + userId

    $http.get(url)
    .success( (data, status, headers, config) ->
      $log.debug "success!"
      $log.debug data
      self.workers = data
    )
    .error( (data, status, headers, config)  ->
      $log.debug "fail!"
      $log.debug data
    )

  @

workersModule.directive 'workersNavigation', ->
  restrict: 'E',
  templateUrl: ASSETS_FOLDER + '/angular_templates/workers_navigation.html'