# views supporint the book list application:  FWIW all views inherit from common/_viewsBase
define ['pages/other/show/templates', 'common/_viewsBase', 'msgbus' ], (Templates, AppViews, msgBus) ->
    # other stuff goes here
    Other: class View extends AppViews.ItemView
        template: _.template(Templates.otherView)
        className: "close"
