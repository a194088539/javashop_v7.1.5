local value = redis.call("get","mytest")

if not value then

    value=0;
end

value=value+1;

redis.call("set","mytest",value)
return true