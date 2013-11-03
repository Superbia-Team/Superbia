# currency list controller
define ["pages/other/show/views", "msgbus"], (Views, msgBus) ->

    otherApp: ->
        view = new Views.Other
        msgBus.events.trigger "app:show", view
