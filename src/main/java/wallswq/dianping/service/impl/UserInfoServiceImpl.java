package wallswq.dianping.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import wallswq.dianping.domian.po.UserInfo;
import wallswq.dianping.mapper.UserInfoMapper;
import wallswq.dianping.service.IUserInfoService;

@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper,UserInfo> implements IUserInfoService {

}
