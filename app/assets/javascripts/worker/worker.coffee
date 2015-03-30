app = angular.module 'WorkerApp', ['TrackingModule']

app.controller 'WorkerController', ['$http', '$log', '$scope', 'geoLocator', ($http, $log, $scope, geoLocator) ->

  ACTIVITIES_WS_URL = "http://localhost:9000/activities/"

  self = @
  @userActivities

  @userId
  @checkInOutLabel
  @lastActivity

  initialize = ->
    self.setUser @userEmail.getAttribute('value')


  ########## Location Services #########

  map = null
  geocoder = null
  latlng = null

  showMap = (position) ->
    latitude = position.coords.latitude
    longitude = position.coords.longitude
    mapOptions = {
      zoom: 15,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    }

    $log.debug 'map element', document.getElementById("map")

    map = new google.maps.Map(document.getElementById("map"), mapOptions)
    latlng = new google.maps.LatLng(latitude, longitude)
    map.setCenter(latlng)

    $log.debug 'map ', map

    geocoder = new google.maps.Geocoder()
    geocoder.geocode({'latLng': latlng}, addAddressToMap)

  addAddressToMap = (results, status) ->
    if (status == google.maps.GeocoderStatus.OK)
      if (results[1])
        marker = new google.maps.Marker({
          position: latlng,
          map: map
        })
        $('#location').html('Your location: ' + results[0].formatted_address)
    else
      alert "Sorry, we were unable to geocode that address."

  ########## Services End #########



  @setUser = (userId) ->
    @userId = userId
    @fetchUserPositions(@userId)

  @updateActivities = (data) ->
    @userActivities = data
    @lastActivity = @userActivities[@userActivities.length - 1]
    @updateCheckInOutLabel()

  @updateCheckInOutLabel = ->
    label = 'Check-In'
    if @userActivities
      if @isCheckout(@lastActivity)
        label = 'Check-Out'
    @checkInOutLabel = label

  @isCheckout = (lastActivity) ->
    #$log.debug 'lastPosition', lastPosition
    lastActivity && lastActivity.in != null && lastActivity.out == null

  @checkInOut = ->
    navigator.geolocation.getCurrentPosition getCurrentPositionSuccess

  getCurrentPositionSuccess = (position) ->
    self.logPosition position

  @logPosition = (position) ->
    showMap(position)
    @postUserPosition @userId, position


  ########## User Activities Services #########

  @postUserPosition = (userId, position) ->
    url = ACTIVITIES_WS_URL + userId + "/lat/"+ position.coords.latitude + "/long/"+ position.coords.longitude

    $http.post(url, position)
    .success( (data, status, headers, config) ->
      $log.debug "success!"
      $log.debug data
      self.updateActivities data
      $log.debug "isCheckOut", self.isCheckout(data)
    )
    .error( (data, status, headers, config)  ->
      $log.debug "fail!"
      $log.debug data
    )

  @fetchUserPositions = (userId) ->
    url = ACTIVITIES_WS_URL + userId

    $http.get(url)
    .success( (data, status, headers, config) ->
      $log.debug "success!"
      self.updateActivities data
    )
    .error( (data, status, headers, config)  ->
      $log.debug "fail!"
      $log.debug data
    )

  ########## User Activities Services #########

  initialize()

  @
]
