/**
 * Created by Andste on 2018/5/2.
 * 用户认证相关API
 */
import { store } from '../redux/store'
import request, { Method } from '../utils/request'
import md5 from 'js-md5'

/**
 * 普通登录
 * @param username
 * @param password
 * @param captcha
 */
export function login(username, password, captcha) {
	return request({
		url: `passport/login`,
		method: Method.GET,
		params: {
			username,
			password: md5(password),
			captcha,
			uuid: store.getState().user.uuid
		}
	})
}

/**
 * 通过手机号登录
 * @param mobile
 * @param sms_code
 */
export function loginByMobile(mobile, sms_code) {
	return request({
		url: `passport/login/${mobile}`,
		method: Method.GET,
		params: {sms_code}
	})
}

/**
 * 发送会员注册手机验证码
 * @param mobile
 * @param captcha
 */
export function sendRegisterSms(mobile, captcha) {
	return request({
		url: `passport/register/smscode/${mobile}`,
		method: Method.POST,
		data: {
			captcha,
			uuid: store.getState().user.uuid
		}
	})
}

/**
 * 发送会员登录手机验证码
 * @param mobile
 * @param captcha
 */
export function sendLoginSms(mobile, captcha) {
	return request({
		url: `passport/login/smscode/${mobile}`,
		method: Method.POST,
		data: {
			captcha,
			uuid: store.getState().user.uuid
		}
	})
}

/**
 * 用户名重复校验
 * @param username
 */
export function checkUsernameRepeat(username) {
	return request({
		url: `passport/username/${username}`,
		method: Method.GET,
		loading: false
	})
}

/**
 * 手机号重复校验
 * @param mobile
 */
export function checkMobileRepeat(mobile) {
	return request({
		url: `passport/mobile/${mobile}`,
		method: Method.GET,
		loading: false
	})
}

/**
 * 注册会员【手机号】
 * @param params
 */
export function registerByMobile(params) {
	params = JSON.parse(JSON.stringify(params))
	params.password = md5(params.password)
	return request({
		url: `passport/register/wap`,
		method: Method.POST,
		data: params
	})
}

/**
 * 验证手机验证码
 * @param mobile   手机号码
 * @param scene    业务场景
 * @param sms_code 短信验证码
 */
export function validMobileSms(mobile, scene, sms_code) {
	return request({
		url: `passport/smscode/${mobile}`,
		method: Method.GET,
		params: {
			scene,
			sms_code
		}
	})
}

/**
 * 验证账户信息
 * @param captcha
 * @param account
 */
export function validAccount(captcha, account) {
	return request({
		url: `passport/find-pwd`,
		method: Method.GET,
		params: {
			uuid: store.getState().user.uuid,
			captcha,
			account
		}
	})
}

/**
 * 发送找回密码短信
 * @param uuid
 * @param captcha
 */
export function sendFindPasswordSms(uuid, captcha) {
	return request({
		url: `passport/find-pwd/send`,
		method: Method.POST,
		data: {
			uuid,
			captcha
		}
	})
}

/**
 * 校验找回密码验证码
 * @param uuid
 * @param sms_code
 */
export function validFindPasswordSms(uuid, sms_code) {
	return request({
		url: `passport/find-pwd/valid`,
		method: Method.GET,
		params: {
			uuid,
			sms_code
		}
	})
}

/**
 * 修改密码【找回密码用】
 * @param password
 * @param uuid
 */
export function changePassword(password, uuid) {
	if (!uuid) uuid = store.getState().user.uuid
	return request({
		url: `passport/find-pwd/update-password`,
		method: Method.PUT,
		data: {
			uuid,
			password: md5(password)
		}
	})
}
