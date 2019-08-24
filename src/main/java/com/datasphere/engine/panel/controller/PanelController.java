package com.datasphere.engine.panel.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.datasphere.core.common.BaseController;
import com.datasphere.engine.core.utils.ExceptionConst;
import com.datasphere.engine.core.utils.JsonWrapper;
import com.datasphere.engine.shaker.workflow.panel.domain.Panel;
import com.datasphere.engine.shaker.workflow.panel.domain.PanelType;
import com.datasphere.engine.shaker.workflow.panel.service.PanelServiceImpl;

import io.micronaut.context.annotation.Parameter;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.Body;
import io.micronaut.validation.Validated;
import io.reactivex.Single;
import org.apache.commons.lang3.StringUtils;

import io.micronaut.http.annotation.Post;

import javax.inject.Inject;

@Validated
public class PanelController extends BaseController {
	@Inject PanelServiceImpl panelServiceImpl;
//	@Inject  ProjectServiceImpl projectServiceImpl;
	public static final String BASE_PATH = "/panel";

	/**
	 * 删除项目下的所有面板
	 * 传入一个项目id，删除这个项目下的所有面板。先检查面板是否能够删除（面板运行中则不能删除），不能删除则返回原因，且设置一个状态值，
	 * 区别删除过程的其他错误原因。
	 */
	@Post(BASE_PATH + "/deletePanelByProjectId")
	public Single<Map<String,Object>> deletePanelByProjectId(@Parameter String projectId) {
		return Single.fromCallable(() -> {
			int status = panelServiceImpl.deleteById(projectId);
			if (0 == status) {
				return JsonWrapper.successWrapper();
			} else {
				return JsonWrapper.failureWrapper(ExceptionConst.DELETE_PANEL_EXISTS_RUNNING_EXCEPTION,
						ExceptionConst.get(ExceptionConst.DELETE_PANEL_EXISTS_RUNNING_EXCEPTION));
			}
		});
	}

	/**1
	 * 创建面板
	 */
	@Post(BASE_PATH + "/createPanel")
	public Single<Map<String,Object>> createPanel(@Body Panel panel, HttpRequest request) {
		return Single.fromCallable(() -> {
			String token = request.getParameters().get("token");
			if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
			// 校验面板名称是否重复
			boolean flag = panelServiceImpl.verifyName(panel.getPanelName(), panel.getProjectId(), panel.getCreator());
			if (flag) {
				return JsonWrapper.failureWrapper(ExceptionConst.NAME_REPEAT, ExceptionConst.get(ExceptionConst.NAME_REPEAT));
			}
			Panel panel2 = panelServiceImpl.create(panel, token);
			if (panel2 == null) return JsonWrapper.failureWrapper("创建项目面板失败！");
			return JsonWrapper.successWrapper(panel2);
		});
	}

	/**
	 * 创建面板
	 */
	@Post(BASE_PATH + "/createcyl")
	public Single<Map<String,Object>> createcyl(@Body Panel panel) {
		return Single.fromCallable(() -> {
			List<Panel> list = new ArrayList<>();
			list.add(panelServiceImpl.getPanelById(null));
			return JsonWrapper.successWrapper(list);
		});
	}

	/**
	 * 查询最后一次访问的面板
	 */
	@Post(BASE_PATH + "/last")
	public Single<Map<String,Object>> getLast(@Body Panel panel1) {
		return Single.fromCallable(() -> {
			return JsonWrapper.successWrapper(panelServiceImpl.getLast(panel1.getCreator()));
		});
	}

	/**
	 * 查询最后一次访问的面板
	 */
	@Post(BASE_PATH + "/lastcyl")
	public Single<Map<String,Object>> getLastcyl(@Parameter String creator) {
		return Single.fromCallable(() -> {
			List<Panel> list = new ArrayList<>();
			Panel panel1 = panelServiceImpl.getLast(creator);
			if (null != panel1) list.add(panel1);
			return JsonWrapper.successWrapper(list);
		});
	}

	/**
	 * 按钮：另存为
	 * @return
	 */
	@Post(BASE_PATH + "/saveAs")
	public Single<Map<String,Object>> saveAs(@Body Panel panel) {
		return Single.fromCallable(() -> {
//			if (!StringUtils.isBlank(panel.getId()) && !StringUtils.isBlank(panel.getPanelName())) {
////				Map<String, String> panelSaveMap = panelServiceImpl.saveAs(id, projectId, panelName, panelDesc);
//				Map<String, String> panelSaveMap = panelServiceImpl.saveAs(panel);
//				if (panelSaveMap != null && panelSaveMap.size() > 0) {
//					return JsonWrapper.successWrapper(panelServiceImpl.panelPageDetail(panelSaveMap.get("projectId"), panelSaveMap.get("id")));
//				} else {
//					return JsonWrapper.failureWrapper("面板另存为失败");
//				}
//			} else {
//				return JsonWrapper.failureWrapper("名称和项目id不为空");
//			}
			if (!StringUtils.isBlank(panel.getPanelName())) {
				if (!panelServiceImpl.verifyName(panel.getPanelName(), panel.getProjectId(), panel.getCreator())) {
					String id = panelServiceImpl.saveAs(panel).getId();
					return JsonWrapper.successWrapper(panelServiceImpl.getPanelById(id));
				} else {
					return JsonWrapper.failureWrapper(ExceptionConst.NAME_REPEAT,
							ExceptionConst.get(ExceptionConst.NAME_REPEAT));
				}
			} else {
				return JsonWrapper.failureWrapper(ExceptionConst.NAMEORID_IS_NULL,
						ExceptionConst.get(ExceptionConst.NAMEORID_IS_NULL));
			}
		});
	}

	@Post(BASE_PATH + "/saveAsCyl")
	public Single<Map<String,Object>> saveAsCyl(@Body Panel panel) {
		return Single.fromCallable(() -> {
			if (!StringUtils.isBlank(panel.getPanelName()) && !StringUtils.isBlank(panel.getProjectId())) {
				if (!panelServiceImpl.verifyName(panel.getPanelName(), panel.getProjectId(), panel.getCreator())) {
					String id = panelServiceImpl.saveAs(panel).getId();
					Panel panel2 = panelServiceImpl.getPanelById(id);
					List<Panel> list = new ArrayList<>();
					list.add(panel2);
					return JsonWrapper.successWrapper(list);
				} else {
					return JsonWrapper.failureWrapper(ExceptionConst.NAME_REPEAT, ExceptionConst.get(ExceptionConst.NAME_REPEAT));
				}
			} else {
				return JsonWrapper.failureWrapper(ExceptionConst.NAMEORID_IS_NULL, ExceptionConst.get(ExceptionConst.NAMEORID_IS_NULL));
			}
		});
	}

	/**2
	 * 更新面板
	 */
	@Post(BASE_PATH + "/update")
	public Single<Map<String,Object>> update(@Body Panel panel) {
		return Single.fromCallable(() -> {
			if (!StringUtils.isBlank(panel.getPanelName()) && !StringUtils.isBlank(panel.getCreator())) {
				boolean flag = panelServiceImpl.verifyName(panel.getPanelName(), panel.getProjectId(), panel.getCreator());
				if (!flag) {
					return JsonWrapper.failureWrapper(ExceptionConst.NAME_REPEAT, ExceptionConst.get(ExceptionConst.NAME_REPEAT));
				}
			} else {
				return JsonWrapper.failureWrapper(ExceptionConst.NAMEORID_IS_NULL, ExceptionConst.get(ExceptionConst.NAMEORID_IS_NULL));
			}
			int num = panelServiceImpl.update(panel);
			if (num > 0) {
				return JsonWrapper.successWrapper("Success");
			} else {
				return JsonWrapper.failureWrapper(ExceptionConst.RECORD_NOT_EXITS, ExceptionConst.get(ExceptionConst.RECORD_NOT_EXITS));
			}
		});
	}

	/**
	 * 删除面板
	 * @param id 面板id
	 * @return
	 */
	@Post(BASE_PATH + "/delete")
	public Single<Map<String,Object>> delete(@Parameter String id) {
		return Single.fromCallable(() -> {
			int status = panelServiceImpl.delete(id);
			if (status != 2) {
				return JsonWrapper.successWrapper(status);
			} else {
				return JsonWrapper.failureWrapper(ExceptionConst.DELETE_PANEL_EXISTS_RUNNING_EXCEPTION,
						ExceptionConst.get(ExceptionConst.DELETE_PANEL_EXISTS_RUNNING_EXCEPTION));
			}
		});
	}

	/**
	 * 根据id获得面板信息
	 *
	 * @param id 面板id
	 * @return
	 */
	@Post(BASE_PATH + "/getPanels")
	public Single<Map<String,Object>> getPanel(@Parameter String id, @Parameter String creator) {
		return Single.fromCallable(() -> {
			if (StringUtils.isBlank(id) || StringUtils.isBlank(creator)) {
				return JsonWrapper.failureWrapper("参数不能为空");
			}
			return JsonWrapper.successWrapper(panelServiceImpl.getPanelById(id));
		});
	}

	/**
	 * 模糊查询
	 * @return
	 */
	@Post(BASE_PATH + "/listBy")
	public Single<Map<String,Object>> listBy(@Body Panel panel) {
		return Single.fromCallable(() -> {
			panel.setType(PanelType.DEFAULT);
			return JsonWrapper.successWrapper(panelServiceImpl.listBy(panel));
		});
	}

	/**
	 * 根据项目id查找该项目下的面板列表，分页，排序功能需要实现
	 *
	 * @param panel 项目id
	 * @return
	 */
	@Post(BASE_PATH + "/getByProjectId")
	public Single<Map<String,Object>> getByProjectId(@Body Panel panel) {
		return Single.fromCallable(() -> {
			panel.setType(PanelType.DEFAULT);
			return JsonWrapper.successWrapper(panelServiceImpl.getPanelsByProjectIdOrdeyPager(panel));
		});
	}

	/**
	 * 根据多个项目id查找每个项目下的面板列表，分页，排序功能需要实现。返回包含所以项目（每个项目id + 对应的面板信息）
	 * @param panel
	 * @return
	 */
	@Post(BASE_PATH + "/getByProjectIdList")
	public Single<Map<String,Object>> getByProjectIdList(@Body Panel panel) {
		return Single.fromCallable(() -> {
			panel.setType(PanelType.DEFAULT);
			return JsonWrapper.successWrapper(panelServiceImpl.getByProjectIdList(panel));
		});
	}

	/** new0
	 * 获取面板详细信息。如果id为null，那么获取最后一次访问的面板ID。如果用户没有面板，那么会使用默认项目创建一个新的面板。
	 * 实际是获取面板的流程信息
	 */
	@Post(BASE_PATH + "/panelPageDetail")
	public Single<Map<String,Object>> panelDetail(@Parameter String projectId, @Parameter String panelId, HttpRequest request) {
		return Single.fromCallable(() -> {
			String token = request.getParameters().get("token");
			if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
			return JsonWrapper.successWrapper(panelServiceImpl.panelDetail(projectId, panelId, token));
		});
	}

	/**
	 * 获取面板详细信息。如果id为null，那么获取最后一次访问的面板ID。如果用户没有面板，那么会使用默认项目创建一个新的面板。
	 */
	@Post(BASE_PATH + "/panelDetail")
	public Single<Map<String,Object>> panelPageDetail(@Parameter String id, @Parameter String panelId, HttpRequest request) {
		return Single.fromCallable(() -> {
			String token = request.getParameters().get("token");
			if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
			return JsonWrapper.successWrapper(panelServiceImpl.panelPageDetail(id, panelId, token));
		});
	}

	/**
	 * 获取面板详细信息。如果id为null，那么获取最后一次访问的面板ID。如果用户没有面板，那么会使用默认项目创建一个新的面板
	 */
	@Post(BASE_PATH + "/panelDetailCyl")
	public Single<Map<String,Object>> panelPageDetailCyl(@Parameter String id, @Parameter String panelId) {
		return Single.fromCallable(() -> {
//			PanelWithAll all = panelServiceImpl.panelPageDetail(id, panelId);
//			List<PanelWithAll> list = new ArrayList<>();
//			list.add(all);
			return JsonWrapper.successWrapper(null);
		});
	}

	/**
	 * 数据源被多少个面板引用，以及是那些面板在引用，返回面板数量和面板信息（包括面板id，面板名称 和 项目id）
	 * @param id 组件id
	 * @param creator
	 * @return
	 */
	@Post(BASE_PATH + "/sourceTrace")
	public Single<Map<String,Object>> sourceTrace(@Parameter String id, @Parameter String creator) {
		return Single.fromCallable(() -> {
			return JsonWrapper.successWrapper(panelServiceImpl.sourceTrace(id, creator));
		});
	}

	/**
	 * 多个数据源中每个数据源被多少个面板引用，以及是那些面板在引用，返回面板数量和面板信息（包括面板id，面板名称 和 项目id）。
	 * @param id
	 * @param creator
	 * @return
	 */
	@Post(BASE_PATH + "/someSourceTrace")
	public Single<Map<String,Object>> someSourceTrace(@Parameter String id, @Parameter String creator) {
		return Single.fromCallable(() -> {
			return JsonWrapper.successWrapper(panelServiceImpl.someSourceTrace(id, creator));
		});
	}

	/**
	 * 验证名称，不允许重命名
	 * @return
	 */
	@Post(BASE_PATH + "/veriftyName")
	public Single<Map<String,Object>> veriftyName(@Parameter String panelName, @Parameter String projectId, @Parameter String creator) {
		return Single.fromCallable(() -> {
			if (!StringUtils.isBlank(panelName) && !StringUtils.isBlank(projectId)) {
				boolean b = panelServiceImpl.verifyName(panelName, projectId, creator);
				if (b) {
					return JsonWrapper.successWrapper();
				}
				return JsonWrapper.failureWrapper(ExceptionConst.NAME_REPEAT,
						ExceptionConst.get(ExceptionConst.NAME_REPEAT));
			} else {
				return JsonWrapper.failureWrapper(ExceptionConst.NAMEORID_IS_NULL,
						ExceptionConst.get(ExceptionConst.NAMEORID_IS_NULL));
			}
		});
	}

	/**
	 * 根据creator获取项目列表
	 * @param panel
	 * @return
	 */
	@Post(BASE_PATH + "/getAllPanelList")
	public Single<Map<String,Object>> getAllPanelList(@Body Panel panel,HttpRequest request) {
		return Single.fromCallable(() -> {
			String token = request.getParameters().get("token");
			if (token == null) return JsonWrapper.jobFailure(-1, "token不能为空！");
			List<Panel> list = panelServiceImpl.getAllPanelList(panel, token);
			return JsonWrapper.successWrapper(list);
		});
	}

	/**
	 * 4.根据id获得项目信息(包含面板列表信息)
	 * @param id 项目id
	 * @return
	 */
	@Post(BASE_PATH + "/getPanel")
	public Single<Map<String,Object>> getPanel(@Parameter String id) {
		return Single.fromCallable(() -> {
			if (StringUtils.isBlank(id)) {
				return JsonWrapper.failureWrapper("参数不能为空");
			}
			return JsonWrapper.successWrapper(panelServiceImpl.getPanelById(id));
		});
	}

}