import { reactive } from 'vue';

// 创建一个包含 loading 状态的响应式对象
const state = reactive({
  isLoading: false,
});

// 导出用于管理 loading 状态的方法
export const setLoadingState = (isLoading) => {
  state.isLoading = isLoading;
};

// 导出响应式对象以供其他组件使用
export const loadingState = state;
