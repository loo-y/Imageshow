(function ($) {

    var defaults = {
        ajaxUrl: 'getimages.jsp',
        imageWidth: 230,
        colsNum: 5,
        imageGap: 10,
        qstring: ""
    }

    var imageNum, $s, keyFun, searchText, isloading, cols=[];

    var $w = $(window);

    Array.prototype.getMin = function () {
        var min = this[0];
        var len = this.length;
        var position = 0;
        for (var i = 1; i < len; i++) {
            if (this[i] < min) {
                min = this[i];
                position = i;
            }
        }
        return { min: min, pos: position };
    }

    // a good way to just get the min for Array.
    //==========================================
    // Array.prototype.min = function(){   
    //    return Math.min.apply({},this)
    //}
    //==========================================

    $.fn.getImages = function (options) {
        var settings = $.extend(true, {}, defaults, options || {});
        $s = $("input#search");

        return this.each(function () {
            applyImageShow($(this), settings.ajaxUrl);
            $w.bind("scroll", { container: $(this) }, scrollHandler);
            $s.bind("keyup keydown", { container: $(this) }, keyHandler);
            $("li", $(this)).live("mouseenter mouseleave", function (e) {
                var e = e || window.event;
                var obj = e.target || e.srcElement;
                $(obj).closest("li").toggleClass("hover");
            });
        });

        function applyImageShow($showDiv, ajaxurl) {
            imageNum = $.ImageNumber || 0;
            $.ajax({
                url: (ajaxurl || settings.ajaxUrl) + "?ff=" + imageNum + settings.qstring,
                timeout: 1000,
                success: function (response) {
                    response.length && outputImages(eval("(" + response + ")"), $showDiv);
                    !response.length && $w.unbind("scroll", scrollHandler);
                }
            });
        }

        function outputImages(imageJson, $container) {
            var imageCount = imageJson.length;
            if (!cols.length) {
                for (var i = 0; i < settings.colsNum; i++) {
                    cols.push(0);
                }
            }
            //cols = [$.h1col || 0, $.h2col || 0, $.h3col || 0, $.h4col || 0];
            for (var i = 0; i < imageCount; i++) {
                var newLink = document.createElement("li");

                var newAnchor = document.createElement("a");
                newAnchor.href = imageJson[i].iu;
                
                var newImage = document.createElement("img");
                newImage.src = imageJson[i].isu;
                newImage.alt = imageJson[i].id;
                //newImage.title = imageJson[i].iname;

                var dlDiv = document.createElement("div");
                dlDiv.className = "dload";
                
                var dlAnchor = newAnchor.cloneNode(false);
                dlAnchor.href = "/download?en="+imageJson[i].en+"&downkey="+imageJson[i].dk;
                //dlAnchor.innerText = "下载";
                dlAnchor.title = "点击下载图片";

                var descDiv = document.createElement("div");
                descDiv.className = "desc";
                descDiv.innerText = imageJson[i].id;

                dlDiv.appendChild(dlAnchor);
                newAnchor.appendChild(newImage);
                newLink.appendChild(newAnchor);
                newLink.appendChild(dlDiv);
                newLink.appendChild(descDiv);

                var MinCol = cols.getMin();
                newLink.style.position = "absolute";
                newLink.className = "imgobj_wrapper";
                newLink.style.width = settings.imageWidth + "px";
                newLink.style.height = imageJson[i].ish + "px";; //thumbnail's height;
                newLink.style.top = MinCol.min + "px";
                newLink.style.left = MinCol.pos * (settings.imageWidth + settings.imageGap) + "px";
                cols[MinCol.pos] += parseInt(imageJson[i].ish) + settings.imageGap;
                $container[0].appendChild(newLink);
            }
            $.ImageNumber = imageNum + imageCount;
            //$.h1col = cols[0];
            //$.h2col = cols[1];
            //$.h3col = cols[2];
            //$.h4col = cols[3];
            isloading = 0;

            
        }

        function keyHandler(e) {
            console.log(e.type);
            e.type == "keydown" && clearTimeout(keyFun);
            if (e.type == "keyup" && searchText != e.target.value) {
                keyFun && clearTimeout(keyFun);
                searchText = e.target.value;
                searchText.trim().length && (keyFun = setTimeout(function () {
                    var $container = e.data.container;
                    clearConatiner($container);
                    settings.qstring = "&desc=" + searchText;
                    applyImageShow($container, settings.ajaxUrl);
                }, 300
                ))
            }
        }

        function scrollHandler(e) {
            if (!isloading && Math.max(document.documentElement.scrollHeight, document.body.scrollHeight) - $w.scrollTop() - $w.height() < 300) {
                isloading = 1;
                applyImageShow(e.data.container, settings.ajaxUrl);
            }
        }

        function clearConatiner($container) {
            $container.empty();
            cols = [];
            $.ImageNumber = 0;
            //$.h1col = $.h2col = $.h3col = $.h4col = $.ImageNumber= 0;
        }
    }

})(jQuery)