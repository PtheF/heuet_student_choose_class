
--
---- Lua 脚本，用于原子的执行Redis抢课业务
---- 
---- 返回状态码：
---- 0:抢课成功
---- 1:重复选课
---- 2:余量不足
--

-- f_6c33de92038305ff32fc14122752168cf088fd2a

-- 课程id
local class_id = ARGV[1]

-- key前缀
local key_prefix = KEYS[1]

-- 课程余量key
local remain_key = key_prefix..':cls:remain:'..class_id
-- 课程已选人数key
local select_key = key_prefix..':cls:select:'..class_id
-- 课程成员set key
local member_key = key_prefix..':cls:member:'..class_id


-- 学生id
local stu_id = ARGV[2]

-- 学生已选课程set
local stu_class_key = key_prefix..':stu:select:'..stu_id

-- 课程类型 1: 任选课  ; 2:体育课
local typ = ARGV[3]


-- 抢课业务id
local sys_id = ARGV[4]

redis.call('set', key_prefix..':lua_redirect', '0')

-- 1. 判断当前学生是否已经选择该课程
if ( tonumber( redis.call('SISMEMBER', member_key, stu_id) ) > 0 ) then
    return 1
end

-- 2. 判断当前课程是否有余量：
--  判断余量这个仅限于体育课
if ( tonumber(typ) == 2 ) then

    if( tonumber( redis.call('get', remain_key) ) <= 0 ) then
        return 2;
    end

end


-- 3. 判断没有问题
redis.call('incr', select_key)
redis.call('incrby', remain_key, -1)
redis.call('sadd', member_key, stu_id)
redis.call('sadd', stu_class_key, class_id)

-- 4. 发送消息
-- 消息队列:stream.rob_class 
-- 消费者组:rob_class_handler

-- 得先创建消息队列：XGROUP CREATE {rob_class}:stream:rob rob_class_handler 0 MKSTREAM
-- 具体的意思是：创建消费者组，监听 stream.rob_class 消费者组名=rob_class_handler，从未处理的第一条开始处理，如果没有队列则创建队列。
--
-- XADD stream.rob_class * robId sys_id uid stu_id cid class_id
redis.call('xadd', key_prefix..':stream:rob', '*', 'rid', sys_id, 'uid', stu_id, 'cid', class_id)


-- 5. 返回结果
return 0;

