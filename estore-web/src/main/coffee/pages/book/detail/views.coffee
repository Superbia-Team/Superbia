# views supporint the book list application:  FWIW all views inherit from common/_viewsBase
define ['underscore', 'pages/book/detail/templates', 'common/_viewsBase', 'msgbus' ], (_, Templates, AppViews, msgbus) ->
    # bookview
    BookDetail: class BookDetailView extends AppViews.ItemView
        template: _.template(Templates.bookdetail)

        modelEvents:
            "change:name" : -> console.log "name changed"

		events:
			"click #close-dialog" : -> @trigger "dialog:close"

		dialog:
			title: "Edit Event"
			className: "dialogClass"
			buttons: false

		onClose: ->
			console.log "view closing"

		onDialogButtonClicked: ->
			console.log "dialog method onDialogButtonClicked"
