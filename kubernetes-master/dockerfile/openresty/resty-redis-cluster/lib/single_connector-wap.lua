local request_uri = "/WAP/" .. ngx.var.uri
local redis = require "resty.redis"
local red = redis:new()

--red:set_timeout(10000) -- 1 sec

local ok, err = red:connect("ip", 6379)
if not ok then
    ngx.say("failed to connect: ", err)
    return
end


local res, err = red:auth("password")
	if not res then
	ngx.say("failed to authenticate: ", err)
	return
end


local res, err = red:get(request_uri)
if not res then
    ngx.say("failed to get: " .. request_uri , err)
    return
end
red:set_keepalive(6000,1000)
ngx.say(res)
