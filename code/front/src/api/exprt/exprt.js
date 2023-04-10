import {
	ApiUtils,
	_axios
} from '@/api/apiUtil.js';
import constant from '@/util/constant.js';

const api = {
	cmd: (params, fileName,type) => {
		ApiUtils.downloadingAjax(constant.serverUrl + "/export/getCmd", params, fileName,type);
	},
	tpv: (params, fileName,type) => {
		ApiUtils.downloadingAjax(constant.serverUrl + "/export/getAllTpv", params,  fileName,type);
	},
	getCrsDoc: (params, fileName,type) => {
		ApiUtils.downloadingAjax(constant.serverUrl + "/export/getCrsDoc", params,fileName,type);
	},
	getTimePlan: (params, successCallback, failCallback, exceptionCallback) => {
		ApiUtils.getLoadingAjax(constant.serverUrl + "/export/getTimeplan", params, successCallback, failCallback, exceptionCallback);
	},

	getTrainplan: (params, successCallback, failCallback, exceptionCallback) => {
		ApiUtils.downloadingAjax(constant.serverUrl + "/export/getTrainplan", params, successCallback, failCallback, exceptionCallback);
	},
 
}

export default api;
