package com.github.emailtohl.building.site.service;

import java.util.Set;

import javax.transaction.Transactional;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;

import com.github.emailtohl.building.common.jpa.Pager;
import com.github.emailtohl.building.site.dto.UserDto;
import com.github.emailtohl.building.site.entities.Authority;

/**
 * 认证和授权服务
 * 本接口还声明了那些复杂的权限逻辑，例如授权，这些逻辑的安全权限不能简单地在接口中声明，只能放在实现类中
 * 
 * @author HeLei
 */
@Validated
@Transactional
public interface AuthenticationService extends AuthenticationProvider {
	/**
	 * 认证用户，由于要返回用户信息，所以只有本人才能调用。
	 * 事实上认证功能已经交给spring security框架完成
	 * @param email
	 * @param password
	 * @return Authentication 代表用户身份的对象
	 */
	Authentication authenticate(String email, String password);

	/**
	 * 判断用户是否具有该权限
	 * @param authorities
	 * @return
	 */
	boolean hasAuthority(Authority ... authorities);
	
	/**
	 * 获取用户权限，需要管理员或MANAGER权限
	 * @param user
	 * @param pageable
	 * @return
	 */
	@NotNull
	@PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
	Pager<UserDto> getPageByRoles(UserDto user, Pageable pageable);
	
	/**
	 * 为用户授权
	 * 授权的逻辑较为复杂，需要在实现类中细化
	 * 例如MANAGER不能为其他人分配ADMIN
	 * 
	 * @param id
	 * @param authorities
	 */
//	@PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
	void grantedRoles(@Min(value = 1L) Long id, @NotNull Set<String> roleNames);

	/**
	 * 检查该邮箱是否注册
	 * @param email
	 * @return
	 */
	boolean isExist(String email);
	
	/**
	 * 修改密码，用于用户忘记密码的场景
	 * @param email
	 * @param newPassword
	 */
	void changePassword(String email, @Pattern(regexp = "^[^\\s&\"<>]+$") String newPassword);

	/**
	 * 更改User的类型，例如将职员提升为经理
	 * 先删除原实体，然后再重新生成新实体，所以会返回新的实体id
	 * @param id
	 * @param role
	 * return 新生成实体的id
	 */
	long updateUserType(long id, String role);
	
}
