import {
	ApiUtils,
	_axios
}from '@/api/apiUtil.js';
import constant from '@/util/constant.js';
const api = {
	list: (params, successCallback, failCallback, exceptionCallback) => {
		ApiUtils.getLoadingAjax(constant.serverUrl + "/subject/coremodel/list", params, successCallback, failCallback, exceptionCallback);
	},
	toImport(params, successCallback, failCallback, exceptionCallback) {
		ApiUtils.getLoadingAjax(constant.serverUrl + "/subject/coremodel/toImport", params, successCallback, failCallback, exceptionCallback);
	},
	save: (params, successCallback, failCallback, exceptionCallback) => {
		ApiUtils.postLoadingAjax(constant.serverUrl + "/subject/coremodel/save", params, successCallback, failCallback, exceptionCallback);
	},
	toUpdate: (params, successCallback, failCallback, exceptionCallback) => {
		ApiUtils.getLoadingAjax(constant.serverUrl + "/subject/coremodel/toUpdate", params, successCallback, failCallback, exceptionCallback);
	},
	update: (params, successCallback, failCallback, exceptionCallback) => {
		ApiUtils.putLoadingAjax(constant.serverUrl + "/subject/coremodel/update", params, successCallback, failCallback, exceptionCallback);
	},
	remove: (params, successCallback, failCallback, exceptionCallback) => {
		ApiUtils.deleteLoadingAjax(constant.serverUrl + "/subject/coremodel/remove", params, successCallback, failCallback, exceptionCallback);
	},
	getBySid: (params, successCallback, failCallback, exceptionCallback) => {
		ApiUtils.getLoadingAjax(constant.serverUrl + "/subject/coremodel/getBySid", params, successCallback, failCallback, exceptionCallback);
	},
	modelExcel: (params, fileName, type) => {
		ApiUtils.downloadingAjax(constant.serverUrl + "/subject/coremodel/modelExcel", params, fileName, type);
	},
}

export default api;
