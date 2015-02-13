angular.module('jscrud', [ 'schemaForm' ]).controller('FormController',
		function($scope) {
			$scope.schema = {
				"title" : "Person",
				"type" : "object",
				"properties" : {
					"age" : {
						"required" : false,
						"type" : "number",
						"format" : "number"
					},
					"gender" : {
						"required" : false,
						"type" : "string",
						"enum" : [ "male", "female", "unknown" ],
						"default" : "unknown"
					},
					"name" : {
						"type" : "string",
						"description" : "give me your name",
						"minLength" : 5
					}
				},
				"required" : [ "name", "age" ]
			};

			$scope.form = [ "*", {
				type : "submit",
				title : "Save"
			} ];

			$scope.model = {};
		});
