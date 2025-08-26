package wallswq.dianping.controller;

import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.web.bind.annotation.*;
import wallswq.dianping.domian.Result;
import wallswq.dianping.service.IFollowService;

@Data
@RestController
@RequestMapping("/follow")
public class FollowController {

    @Resource
    private IFollowService followService;

    @GetMapping("or/not/{id}")
    public Result follow(@PathVariable(name = "id") Long followUserId) {
        return followService.follow(followUserId);
    }
    @PutMapping("{id}/{isFollow}")
    public Result isFollow(@PathVariable(name = "id") Long followUserId, @PathVariable("isFollow") boolean isFollow) {
        return followService.isFollow(followUserId,isFollow);

    }
    @GetMapping("common/{id}")
    public Result commonFollow(@PathVariable(name = "id") Long id) {
        return followService.commonFollow(id);
    }

}
