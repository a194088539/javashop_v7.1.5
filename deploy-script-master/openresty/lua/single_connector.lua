local request_uri = ngx.var.uri 
local redis = require "resty.redis"
local red = redis:new()

--red:set_timeout(10000) -- 1 sec

local ok, err = red:connect("192.168.2.5", 6379)
if not ok then
    ngx.say("failed to connect: ", err)
    return
end

--如果设置了密码请打开注释，并填写密码
--local res, err = red:auth("password")
--	if not res then
--	ngx.say("failed to authenticate: ", err)
--	return
--end

local res, err = red:get(request_uri)
-- 请修改https://www.test.com 为实际配置域名或IP端口
if res == ngx.null then
    ngx.redirect("https://www.test.com/404.html")
   return
end

if not res then
    ngx.say("failed to get: " .. request_uri , err)
    return
end
red:set_keepalive(6000,1000)
ngx.say(res)
