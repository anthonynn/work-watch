geolocationModule = angular.module 'geolocationModule', []
geolocationModule.factory 'geoLocator', ->


  map = null
  mapCenter = null
  geocoder = null
  latlng = null
  timeoutId = null

  initialize = ->
    if Modernizr.geolocation
      navigator.geolocation.getCurrentPosition showMap

  showMap = (position) ->
    latitude = position.coords.latitude
    longitude = position.coords.longitude
    mapOptions = {
      zoom: 15,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    }
    map = new google.maps.Map(document.getElementById("map"), mapOptions)
    latlng = new google.maps.LatLng(latitude, longitude)
    map.setCenter(latlng)

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

  start = ->
    timeoutId = setTimeout initialize, 500

  reset = ->
    if (timeoutId)
      clearTimeout timeoutId

  @Map = {
    start: start
    reset: reset
  }