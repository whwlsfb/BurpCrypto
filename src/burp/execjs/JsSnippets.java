package burp.execjs;

import java.util.HashMap;

public class JsSnippets {
    public static HashMap<String, String> SnippetHelps = new HashMap<String,String>() {{
       put("Base64",
               "\r\n/* \n" +
                   " * Base64 example:\n" +
                   " * base64encode('admin');\n" +
                   " * > YWRtaW4=\n" +
                   " * base64decode('YWRtaW4=');\n" +
                   " * > admin\n" +
                   " */");
        put("MD5",
                "\r\n/* \n" +
                    " * MD5 example:\n" +
                    " * md5('123456');\n" +
                    " * > e10adc3949ba59abbe56e057f20f883e\n"+
                    " */");
    }};
    public static HashMap<String, String> Snippets = new HashMap<String, String>() {{
            put("Base64","__b64 = {};\n" +
                    "(function(b, c) {\n" +
                    "    var a = function() {\n" +
                    "        var g = \"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/\";\n" +
                    "        function d(r) {\n" +
                    "            if (/([^\\u0000-\\u00ff])/.test(r)) {\n" +
                    "                throw new Error(\"INVALID_CHARACTER_ERR\");\n" +
                    "            }\n" +
                    "            var q = 0, t, o, p, l = [];\n" +
                    "            while (q < r.length) {\n" +
                    "                o = r.charCodeAt(q);\n" +
                    "                p = q % 3;\n" +
                    "                switch (p) {\n" +
                    "                case 0:\n" +
                    "                    l.push(g.charAt(o >> 2));\n" +
                    "                    break;\n" +
                    "                case 1:\n" +
                    "                    l.push(g.charAt((t & 3) << 4 | (o >> 4)));\n" +
                    "                    break;\n" +
                    "                case 2:\n" +
                    "                    l.push(g.charAt((t & 15) << 2 | (o >> 6)));\n" +
                    "                    l.push(g.charAt(o & 63));\n" +
                    "                    break;\n" +
                    "                }\n" +
                    "                t = o;\n" +
                    "                q++;\n" +
                    "            }\n" +
                    "            if (p == 0) {\n" +
                    "                l.push(g.charAt((t & 3) << 4));\n" +
                    "                l.push(\"==\");\n" +
                    "            } else {\n" +
                    "                if (p == 1) {\n" +
                    "                    l.push(g.charAt((t & 15) << 2));\n" +
                    "                    l.push(\"=\");\n" +
                    "                }\n" +
                    "            }\n" +
                    "            return l.join(\"\");\n" +
                    "        }\n" +
                    "        function f(q) {\n" +
                    "            q = q.replace(/\\s|=/g, \"\");\n" +
                    "            var t, r, p, o = 0, l = [];\n" +
                    "            while (o < q.length) {\n" +
                    "                t = g.indexOf(q.charAt(o));\n" +
                    "                p = o % 4;\n" +
                    "                switch (p) {\n" +
                    "                case 0:\n" +
                    "                    break;\n" +
                    "                case 1:\n" +
                    "                    l.push(String.fromCharCode(r << 2 | t >> 4));\n" +
                    "                    break;\n" +
                    "                case 2:\n" +
                    "                    l.push(String.fromCharCode((r & 15) << 4 | t >> 2));\n" +
                    "                    break;\n" +
                    "                case 3:\n" +
                    "                    l.push(String.fromCharCode((r & 3) << 6 | t));\n" +
                    "                    break;\n" +
                    "                }\n" +
                    "                r = t;\n" +
                    "                o++;\n" +
                    "            }\n" +
                    "            return l.join(\"\");\n" +
                    "        }\n" +
                    "        var e = {\n" +
                    "            btoa: d,\n" +
                    "            atob: f,\n" +
                    "            encode: d,\n" +
                    "            decode: f\n" +
                    "        };\n" +
                    "        return e;\n" +
                    "    }();\n" +
                    "    if (!b.Base64_3) {\n" +
                    "        b.Base64_3 = a;\n" +
                    "    }\n" +
                    "    if (!b.btoa) {\n" +
                    "        b.btoa = a.btoa;\n" +
                    "    }\n" +
                    "    if (!b.atob) {\n" +
                    "        b.atob = a.atob;\n" +
                    "    }\n" +
                    "}\n" +
                    ")(__b64);\n" +
                    "function base64encode(a) {\n" +
                    "    return __b64.btoa(a);\n" +
                    "}\n" +
                    "function base64decode(a) {\n" +
                    "    return __b64.atob(a);\n" +
                    "}");
            put("MD5","!function(n){\"use strict\";function d(n,t){var r=(65535&n)+(65535&t);return(n>>16)+(t>>16)+(r>>16)<<16|65535&r}function f(n,t,r,e,o,u){return d((c=d(d(t,n),d(e,u)))<<(f=o)|c>>>32-f,r);var c,f}function l(n,t,r,e,o,u,c){return f(t&r|~t&e,n,t,o,u,c)}function v(n,t,r,e,o,u,c){return f(t&e|r&~e,n,t,o,u,c)}function g(n,t,r,e,o,u,c){return f(t^r^e,n,t,o,u,c)}function m(n,t,r,e,o,u,c){return f(r^(t|~e),n,t,o,u,c)}function i(n,t){var r,e,o,u;n[t>>5]|=128<<t%32,n[14+(t+64>>>9<<4)]=t;for(var c=1732584193,f=-271733879,i=-1732584194,a=271733878,h=0;h<n.length;h+=16)c=l(r=c,e=f,o=i,u=a,n[h],7,-680876936),a=l(a,c,f,i,n[h+1],12,-389564586),i=l(i,a,c,f,n[h+2],17,606105819),f=l(f,i,a,c,n[h+3],22,-1044525330),c=l(c,f,i,a,n[h+4],7,-176418897),a=l(a,c,f,i,n[h+5],12,1200080426),i=l(i,a,c,f,n[h+6],17,-1473231341),f=l(f,i,a,c,n[h+7],22,-45705983),c=l(c,f,i,a,n[h+8],7,1770035416),a=l(a,c,f,i,n[h+9],12,-1958414417),i=l(i,a,c,f,n[h+10],17,-42063),f=l(f,i,a,c,n[h+11],22,-1990404162),c=l(c,f,i,a,n[h+12],7,1804603682),a=l(a,c,f,i,n[h+13],12,-40341101),i=l(i,a,c,f,n[h+14],17,-1502002290),c=v(c,f=l(f,i,a,c,n[h+15],22,1236535329),i,a,n[h+1],5,-165796510),a=v(a,c,f,i,n[h+6],9,-1069501632),i=v(i,a,c,f,n[h+11],14,643717713),f=v(f,i,a,c,n[h],20,-373897302),c=v(c,f,i,a,n[h+5],5,-701558691),a=v(a,c,f,i,n[h+10],9,38016083),i=v(i,a,c,f,n[h+15],14,-660478335),f=v(f,i,a,c,n[h+4],20,-405537848),c=v(c,f,i,a,n[h+9],5,568446438),a=v(a,c,f,i,n[h+14],9,-1019803690),i=v(i,a,c,f,n[h+3],14,-187363961),f=v(f,i,a,c,n[h+8],20,1163531501),c=v(c,f,i,a,n[h+13],5,-1444681467),a=v(a,c,f,i,n[h+2],9,-51403784),i=v(i,a,c,f,n[h+7],14,1735328473),c=g(c,f=v(f,i,a,c,n[h+12],20,-1926607734),i,a,n[h+5],4,-378558),a=g(a,c,f,i,n[h+8],11,-2022574463),i=g(i,a,c,f,n[h+11],16,1839030562),f=g(f,i,a,c,n[h+14],23,-35309556),c=g(c,f,i,a,n[h+1],4,-1530992060),a=g(a,c,f,i,n[h+4],11,1272893353),i=g(i,a,c,f,n[h+7],16,-155497632),f=g(f,i,a,c,n[h+10],23,-1094730640),c=g(c,f,i,a,n[h+13],4,681279174),a=g(a,c,f,i,n[h],11,-358537222),i=g(i,a,c,f,n[h+3],16,-722521979),f=g(f,i,a,c,n[h+6],23,76029189),c=g(c,f,i,a,n[h+9],4,-640364487),a=g(a,c,f,i,n[h+12],11,-421815835),i=g(i,a,c,f,n[h+15],16,530742520),c=m(c,f=g(f,i,a,c,n[h+2],23,-995338651),i,a,n[h],6,-198630844),a=m(a,c,f,i,n[h+7],10,1126891415),i=m(i,a,c,f,n[h+14],15,-1416354905),f=m(f,i,a,c,n[h+5],21,-57434055),c=m(c,f,i,a,n[h+12],6,1700485571),a=m(a,c,f,i,n[h+3],10,-1894986606),i=m(i,a,c,f,n[h+10],15,-1051523),f=m(f,i,a,c,n[h+1],21,-2054922799),c=m(c,f,i,a,n[h+8],6,1873313359),a=m(a,c,f,i,n[h+15],10,-30611744),i=m(i,a,c,f,n[h+6],15,-1560198380),f=m(f,i,a,c,n[h+13],21,1309151649),c=m(c,f,i,a,n[h+4],6,-145523070),a=m(a,c,f,i,n[h+11],10,-1120210379),i=m(i,a,c,f,n[h+2],15,718787259),f=m(f,i,a,c,n[h+9],21,-343485551),c=d(c,r),f=d(f,e),i=d(i,o),a=d(a,u);return[c,f,i,a]}function a(n){for(var t=\"\",r=32*n.length,e=0;e<r;e+=8)t+=String.fromCharCode(n[e>>5]>>>e%32&255);return t}function h(n){var t=[];for(t[(n.length>>2)-1]=void 0,e=0;e<t.length;e+=1)t[e]=0;for(var r=8*n.length,e=0;e<r;e+=8)t[e>>5]|=(255&n.charCodeAt(e/8))<<e%32;return t}function e(n){for(var t,r=\"0123456789abcdef\",e=\"\",o=0;o<n.length;o+=1)t=n.charCodeAt(o),e+=r.charAt(t>>>4&15)+r.charAt(15&t);return e}function r(n){return unescape(encodeURIComponent(n))}function o(n){return a(i(h(t=r(n)),8*t.length));var t}function u(n,t){return function(n,t){var r,e,o=h(n),u=[],c=[];for(u[15]=c[15]=void 0,16<o.length&&(o=i(o,8*n.length)),r=0;r<16;r+=1)u[r]=909522486^o[r],c[r]=1549556828^o[r];return e=i(u.concat(h(t)),512+8*t.length),a(i(c.concat(e),640))}(r(n),r(t))}function t(n,t,r){return t?r?u(t,n):e(u(t,n)):r?o(n):e(o(n))}\"function\"==typeof define&&define.amd?define(function(){return t}):\"object\"==typeof module&&module.exports?module.exports=t:n.md5=t}(this);");
        }};
    public static String EmptyFunction =
            "\r\n" +
            "function calc(pass) {\n" +
            "    return pass;\n" +
            "}\r\n";
}
