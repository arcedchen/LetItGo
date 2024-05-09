<template>
  <div class="layout">
    <div class="aside">
      <div class="line"></div>
      <ul class="menu">
        <li class="submenu">
          <span><i class="bi-clipboard-minus"></i> 全站管理</span>
          <ul class="submenu-items">
            <li>
              <router-link to="/manager/member">全站會員管理</router-link>
            </li>
            <li>
              <router-link to="/manager/productCategory"
                >全站產品分類</router-link
              >
            </li>
            <li>
              <router-link to="/manager/allTrade">全站交易紀錄</router-link>
            </li>
            <li>
              <router-link to="/manager/report">全站檢舉管理</router-link>
            </li>
          </ul>
        </li>
      </ul>
    </div>
    <div class="content">
      <div class="main">
        <router-view></router-view>
      </div>
    </div>
  </div>
</template>

<script>
export default {};
</script>

<style scoped>
.content {
  flex: 1;
  /* overflow-y: auto;  */
}

.main {
  padding: 20px;
}

.layout {
  height: 880px;
  /* min-height: 500px; */
  display: flex;
}

/* .aside {
  width: 200px;
  background-color: #222832;
  color: white;
} */

.aside {
  flex: 0 0 250px;
  background-color: #222832;
  color: white;
}

.head {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 50px;
}

.head img {
  width: 50px;
  height: 50px;
  margin-right: 10px;
}

.head span {
  font-size: 20px;
}

.line {
  border-top: 1px solid hsla(0, 0%, 100%, 0.05);
  border-bottom: 1px solid rgba(0, 0, 0, 0.2);
}

.menu {
  list-style: none;
  padding: 0;
}

.submenu {
  padding: 10px;
}

.submenu span {
  cursor: pointer;
}

.submenu-items {
  list-style: none;
  padding: 0;
  margin-left: 20px;
}

.submenu-items li {
  padding: 5px 0;
}

.submenu-items a {
  color: white;
  text-decoration: none;
  transition: color 0.3s ease;
}

.submenu-items a:hover {
  color: #409eff;
}
</style>
