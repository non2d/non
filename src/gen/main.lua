non.ready = function()
    -- Insert initialization logic here
end

non.update = function(dt)
    -- Insert update logic here
end

non.draw = function()
    -- Insert rendering logic here
    graphics:begin()
    graphics:print("Hello World!", 15, 15)
    graphics:display()
end