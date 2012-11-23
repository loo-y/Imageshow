(function ($) {
    var defaults = {
        ByUrlClass: "byurl",
        ByUploadClass: "byup"
    };

    $.fn.getUpload = function (options) {
        var settings = $.extend(true, {}, defaults, options || {});
        return this.each(function () {
            applyUpload($(this));
        });

        function applyUpload($container) {
            $("input", $container).change(function () {
                if (this.checked) {
                    var $ByUrlClass = $("." + settings.ByUrlClass);
                    var $ByUploadClass = $("." + settings.ByUploadClass);
                    this.value == settings.ByUrlClass && $ByUploadClass.hide() && $ByUrlClass.show();
                    this.value == settings.ByUploadClass && $ByUrlClass.hide() && $ByUploadClass.show();
                }
            })
        }
    }

})(jQuery)