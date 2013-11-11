# main.coffee requireJS bootloader file typically included in the index.html
require.config
    baseURL: 'js',

    paths:
        jquery: '../lib/jquery/jquery-2.0.3.min'
        underscore: '../lib/underscore/underscore-min'
        backbone: '../lib/backbone/backbone'
        'backbone.wreqr': '../lib/backbone.wreqr/backbone.wreqr.min'
        'backbone.eventbinder': '../lib/backbone.eventbinder/backbone.eventbinder.min'
        'backbone.babysitter': '../lib/backbone.babysitter/backbone.babysitter.min'
        marionette: '../lib/marionette/backbone.marionette'
        bootstrap: '../lib/bootstrap/bootstrap'
        text: '../lib/requirejs/text',
        templates: '../../templates'

    shim:
        backbone:
            deps: [ 'underscore', 'jquery' ]
            exports: 'Backbone'
        underscore:
            exports: '_'
        bootstrap:
            deps: [ 'jquery' ]
            exports: '$.fn.button'

    # start the main APP but let require also load any pre-app config and teh book and other apps
    require ["config/_base", "app",  "pages/book/app", "pages/other/app" ], (_config, App) ->
        App.start()
