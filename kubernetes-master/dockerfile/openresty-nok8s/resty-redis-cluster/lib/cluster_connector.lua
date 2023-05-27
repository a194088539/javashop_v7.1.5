local request_uri = ngx.var.uri
local config = {
    name = "testCluster",                   --rediscluster name
    -- enableSlaveRead = true,
    serv_list = {                           --redis cluster node list(host and port),
        { ip = "ip", port = port },
        { ip = "ip", port = port }
    },
    keepalive_timeout = 60000,              --redis connection pool idle timeout
    keepalive_cons = 300,                  --redis connection pool size
    connection_timout = 15000,               --timeout while connecting
    max_redirection = 5                     --maximum retry attempts for redirection
}

local redis_cluster = require "resty.rediscluster"
local red_c = redis_cluster:new(config)

local v, err = red_c:get(request_uri)
if err then
    ngx.log(ngx.ERR, "err: ", err)
else
    ngx.say(v)
end
