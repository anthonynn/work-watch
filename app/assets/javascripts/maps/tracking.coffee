app = angular.module 'TrackingModule', ['geolocationModule']
ASSETS_FOLDER = "assets"

app.directive 'checkInOut', ->
  restrict: 'E',
  templateUrl: ASSETS_FOLDER + '/angular_templates/check_in_out.html'

app.directive 'activityLog', ->
  restrict: 'E',
  templateUrl: ASSETS_FOLDER + '/angular_templates/activity_log.html'

app.directive 'activityMap', ->
  restrict: 'E',
  templateUrl: ASSETS_FOLDER + '/angular_templates/activity_map.html'