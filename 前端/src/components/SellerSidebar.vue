<template>
  <div class="layout">
    <div class="aside">
      <div class="line"></div>
      <ul class="menu">
        <li class="submenu">
          <span><i class="bi-clipboard-minus"></i> 訂單管理</span>
          <ul class="submenu-items">
            <li>
              <router-link to="/seller/order">所有訂單</router-link>
            </li>
            <li>
              <router-link to="/seller/orderUnpaid">未付款訂單</router-link>
            </li>
            <li>
              <router-link to="/seller/orderToShip">待出貨訂單</router-link>
            </li>
            <li>
              <router-link to="/seller/orderShipping">運送中訂單</router-link>
            </li>
            <li>
              <router-link to="/seller/orderCompleted">已完成訂單</router-link>
            </li>
            <li>
              <router-link to="/seller/orderCancelled">不成立訂單</router-link>
            </li>
            <li>
              <router-link to="/seller/orderReturn">退貨/退款</router-link>
            </li>
          </ul>
        </li>
        <li class="submenu">
          <span><i class="bi-cart"></i> 商品管理</span>
          <ul class="submenu-items">
            <li><router-link to="/seller/products">我的商品</router-link></li>
            <li><router-link to="/seller/addproduct">新增商品</router-link></li>
            <!-- <li><router-link to="/seller/violations">商品違規</router-link></li> -->
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
  /* overflow-y: auto; */
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
