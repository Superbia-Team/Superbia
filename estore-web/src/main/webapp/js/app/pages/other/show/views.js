// Generated by CoffeeScript 1.6.3
(function() {
  var __hasProp = {}.hasOwnProperty,
    __extends = function(child, parent) { for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; };

  define(['pages/other/show/templates', 'views/_base', 'msgbus'], function(Templates, AppViews, msgBus) {
    var View, _ref;
    return {
      Other: View = (function(_super) {
        __extends(View, _super);

        function View() {
          _ref = View.__super__.constructor.apply(this, arguments);
          return _ref;
        }

        View.prototype.template = _.template(Templates.otherView);

        View.prototype.className = "close";

        return View;

      })(AppViews.ItemView)
    };
  });

}).call(this);
