import {
	ApiUtils,
	_axios
}from '@/api/apiUtil.js';
import constant from '@/util/constant.js';
const api = {
	removePjbztk: (params, successCallback, failCallback, exceptionCallback) => {
		ApiUtils.deleteLoadingAjax(constant.serverUrl + "/subject/coursestandards/removePjbztk", params, successCallback, failCallback, exceptionCallback);
	},
	listPjbztkVo: (params, successCallback, failCallback, exceptionCallback) => {
		ApiUtils.getLoadingAjax(constant.serverUrl + "/subject/coursestandards/listPjbztkVo", params, successCallback, failCallback, exceptionCallback);
	},
	updatePjbztk: (params, successCallback, failCallback, exceptionCallback) => {
		ApiUtils.getLoadingAjax(constant.serverUrl + "/subject/coursestandards/updatePjbztk", params, successCallback, failCallback, exceptionCallback);
	},
	list: (params, successCallback, failCallback, exceptionCallback) => {
		ApiUtils.getLoadingAjax(constant.serverUrl + "/subject/coursestandards/list", params, successCallback, failCallback, exceptionCallback);
	},savePjbztk: (params, successCallback, failCallback, exceptionCallback) => {
		ApiUtils.postLoadingAjax(constant.serverUrl + "/subject/coursestandards/updateAllPjbztk", params, successCallback, failCallback, exceptionCallback);
	},
	save: (params, successCallback, failCallback, exceptionCallback) => {
		ApiUtils.postLoadingAjax(constant.serverUrl + "/subject/coursestandards/save", params, successCallback, failCallback, exceptionCallback);
	},
	toUpdate: (params, successCallback, failCallback, exceptionCallback) => {
		ApiUtils.getLoadingAjax(constant.serverUrl + "/subject/coursestandards/toUpdate", params, successCallback, failCallback, exceptionCallback);
	},
	update: (params, successCallback, failCallback, exceptionCallback) => {
		ApiUtils.putLoadingAjax(constant.serverUrl + "/subject/coursestandards/update", params, successCallback, failCallback, exceptionCallback);
	},
	remove: (params, successCallback, failCallback, exceptionCallback) => {
		ApiUtils.deleteLoadingAjax(constant.serverUrl + "/subject/coursestandards/remove", params, successCallback, failCallback, exceptionCallback);
	},
	exportCrsDoc: (params, fileName,type) => {
		ApiUtils.downloadingAjax(constant.serverUrl + "/subject/coursestandards/exportCrsDoc", params,fileName,type);
	},
}

export default api;
