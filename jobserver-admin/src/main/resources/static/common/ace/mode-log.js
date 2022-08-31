ace.define("ace/mode/log_highlight_rules", ["require", "exports", "module", "ace/lib/oop", "ace/mode/text_highlight_rules"], function (e, t, n) {
    "use strict";
    var r = e("../lib/oop"), i = e("./text_highlight_rules").TextHighlightRules, s = function () {
        var e = "[ERROR]", i = this.createKeywordMapper({
            keyword: e
        }, "identifier", !0);
        this.$rules = {
            start: [{token: i, regex: "\[[a-zA-Z_$][a-zA-Z0-9_$]*\\b\]"}]
        }, this.normalizeRules()
    };
    r.inherits(s, i), t.SqlHighlightRules = s
}), ace.define("ace/mode/log", ["require", "exports", "module",  "ace/mode/log_highlight_rules"], function (e, t, n) {
    "use strict";
    var r = e("../lib/oop"), i = e("./text").Mode, s = e("./log_highlight_rules").SqlHighlightRules, o = function () {
        this.HighlightRules = s, this.$behaviour = this.$defaultBehaviour
    };
    r.inherits(o, i), function () {
        this.lineCommentStart = "--", this.$id = "ace/mode/log"
    }.call(o.prototype), t.Mode = o
})