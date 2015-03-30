app = angular.module 'SupervisorApp', ['WorkersModule', 'TrackingModule']
ASSETS_FOLDER = "assets"


app.controller 'SupervisorController', ['$http', '$log', '$scope', 'workersController', ($http, $log, $scope, workersController) ->

  SUPERVISOR_WS_URL = "http://localhost:9000/supervisor/"
  WORKERS_WS_URL = "http://localhost:9000/workers/"

  self = @
  #@user = {"email": "", "project" : ""}

  @workers = []

  initialize = ->
    $log.debug 'initializing workers'
    fetchWorkers("project")

  #use workers.js
  fetchWorkers = (userId) ->
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

  ACTIVITIES_WS_URL = "http://localhost:9000/activities/"
  @userPositions = []
  @userId

  @setUser = (userId) ->
    @userId = userId
    @fetchUserPositions(@userId)

  #use tracking.js
  @fetchUserPositions = (userId) ->
    url = ACTIVITIES_WS_URL + userId

    $http.get(url)
    .success( (data, status, headers, config) ->
      $log.debug "success!"
      $log.debug data
      self.userPositions = data
    )
    .error( (data, status, headers, config)  ->
      $log.debug "fail!"
      $log.debug data
    )

  initialize()

  @
]


