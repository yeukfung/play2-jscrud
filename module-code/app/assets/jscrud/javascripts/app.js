var app = angular.module('jscrud', [ 'schemaForm', 'ngRoute', 'ngResource' ,'jsonFormatter']);

app.constant('cfg', {
	jsonSchemaUrl : window.jsonSchemaUrl,
	restUrl : window.restUrl,
	baseAssetUrl : "../../jscrud/assets/"
});

app.factory('RestAPI', [ '$resource', 'cfg', function($resource, cfg) {
	return $resource(cfg.restUrl, null, {
		'update' : {
			method : 'PUT',
			params : {
				id : '@id'
			}
		}
	});
} ]);

app.config([ '$routeProvider', '$resourceProvider', 'cfg',
		function($routeProvider, $resourceProvider, cfg) {
			var baseUrl = cfg.baseAssetUrl;

			$routeProvider.when('/', {
        templateUrl : baseUrl + 'app/partials/item-list.html',
        controller : 'ItemListController'
      }).when('', {
        templateUrl : baseUrl + 'app/partials/item-list.html',
        controller : 'ItemListController'
      }).when('/edit/:id', {
				templateUrl : baseUrl + '/app/partials/item-detail.html',
				controller : 'ItemDetailController'
			}).when('/new', {
				templateUrl : baseUrl + '/app/partials/item-detail.html',
				controller : 'ItemDetailController'
			}).otherwise({
				redirectTo : '/'
			});
			$resourceProvider.defaults.stripTrailingSlashes = false;

		} ]);

app.controller('ItemListController', [ '$scope', 'RestAPI',
		function($scope, RestAPI) {
			$scope.items = RestAPI.query();

			$scope.prettyJs = function(js) {
				return JSON.stringify(js, undefined, 1).trim() ;
			}
		} ]);

app.controller('ItemDetailController', [ '$scope', '$http', '$routeParams',
		'RestAPI', '$location', 'cfg',
		function($scope, $http, $routeParams, RestAPI, $location, cfg) {
			$http.get(cfg.jsonSchemaUrl).success(function(data) {
				$scope.schema = data;
			});

			$scope.schema = {};

			$scope.form = [ "*", {
				"type" : "actions",
				"items" : [ {
					"type" : "submit",
					"style" : "btn-info",
					"title" : "Save"
				}, {
					"type" : "button",
					"style" : "btn-danger",
					"title" : "Delete",
					"onClick" : "onDelete()"
				} ]
			} ];

			var itemId = $routeParams.id;

			if (itemId) {
				$scope.sectionTitle = "Edit"
				$scope.model = RestAPI.get({
					id : itemId
				}, function() {
					// good code
				}, function(response) {
					// 404 or bad
					if (response.status === 404) {
						$location.path('/');
					}
				});
			} else {
				$scope.sectionTitle = "Add new"
				$scope.model = {};
			}

			$scope.onSave = function(form, model) {
			    $scope.$broadcast('schemaFormValidate');

				if (form.$valid) {
					if ($scope.model.id) {
					  RestAPI.update($scope.model);
					} else {
					  RestAPI.save($scope.model, function(result) {
							$location.path('/edit/' + result.id);
						});
					}
				}
			}

			$scope.onDelete = function() {
				if ($scope.model.id) {
				  RestAPI.remove($scope.model);
				}
				$location.path('/');
			}

			$scope.sayNo = function() {
				alert("oh no!");
			}
		} ]);
