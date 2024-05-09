import { createRouter, createWebHistory } from "vue-router";
import { setLoadingState } from "@/stores/LoadingState.js";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      component: () => import("@/views/buyer/HomePage.vue"),
      meta: {
        buyerpage: true,
      },
    },
    {
      path: "/login",
      component: () => import("@/views/buyer/LoginPage.vue"),
      meta: {
        buyerpage: true,
      },
    },
    {
      path: "/login/check",
      component: () => import("@/views/buyer/LoginCheck.vue"),
      meta: {
        buyerpage: true,
      },
    },
    {
      path: "/register",
      component: () => import("@/views/buyer/RegisterPage.vue"),
      meta: {
        buyerpage: true,
      },
    },
    {
      path: "/profile",
      component: () => import("@/views/buyer/MemberProfile.vue"),
      meta: {
        buyerpage: true,
      },
    },
    {
      path: "/search",
      component: () => import("@/views/buyer/SearchPage.vue"),
      meta: {
        buyerpage: true,
      },
    },
    {
      path: "/order",
      component: () => import("@/views/buyer/OrderPage.vue"),
      meta: {
        buyerpage: true,
      },
    },
    {
      path: "/product/:id",
      component: () => import("@/views/buyer/ProductPage.vue"),
      meta: {
        buyerpage: true,
      },
    },
    {
      path: "/member/:id",
      component: () => import("@/views/buyer/MemberPage.vue"),
      meta: {
        buyerpage: true,
      },
    },
    {
      path: "/cart",
      component: () => import("@/views/buyer/ShoppingCart.vue"),
      meta: {
        buyerpage: true,
      },
    },
    {
      path: "/wallet",
      component: () => import("@/views/buyer/Wallet.vue"),
      meta: {
        buyerpage: true,
      },
    },
    {
      path: "/manager",
      component: () => import("@/views/manager/Manager.vue"),
      meta: {
        managerpage: true,
      },
      children: [
        {
          path: "productCategory",
          component: () => import("@/views/manager/ProductCategory.vue"),
          meta: {
            managerpage: true,
          },
        },
        {
          path: "allTrade",
          component: () => import("@/views/manager/Trade.vue"),
          meta: {
            managerpage: true,
          },
        },
        {
          path: "report",
          component: () => import("@/views/manager/Report.vue"),
          meta: {
            managerpage: true,
          },
        },
        {
          path: "member",
          component: () => import("@/views/manager/Member.vue"),
          meta: {
            managerpage: true,
          },
        },
      ],
    },
    {
      path: "/seller",
      component: () => import("@/views/seller/Seller.vue"),
      meta: {
        sellerpage: true,
      },
      children: [
        {
          path: "order",
          component: () => import("@/views/seller/Order.vue"),
          meta: {
            sellerpage: true,
          },
        },
        {
          path: "orderUnpaid",
          component: () => import("@/views/seller/OrderUnpaid.vue"),
          meta: {
            sellerpage: true,
          },
        },
        {
          path: "orderToShip",
          component: () => import("@/views/seller/OrderToShip.vue"),
          meta: {
            sellerpage: true,
          },
        },
        {
          path: "orderShipping",
          component: () => import("@/views/seller/OrderShipping.vue"),
          meta: {
            sellerpage: true,
          },
        },
        {
          path: "orderCompleted",
          component: () => import("@/views/seller/OrderCompleted.vue"),
          meta: {
            sellerpage: true,
          },
        },
        {
          path: "orderCancelled",
          component: () => import("@/views/seller/OrderCancelled.vue"),
          meta: {
            sellerpage: true,
          },
        },
        {
          path: "orderReturn",
          component: () => import("@/views/seller/OrderReturn.vue"),
          meta: {
            sellerpage: true,
          },
        },
        {
          path: "products",
          component: () => import("@/views/seller/Products.vue"),
          meta: {
            sellerpage: true,
          },
        },
        {
          path: "addproduct",
          component: () => import("@/views/seller/AddProduct.vue"),
          meta: {
            sellerpage: true,
          },
        },
      ],
    },
    {
      path: "/:pathMatch(.*)*",
      component: () => import("@/views/NotFound.vue"),
      meta: {
        buyerpage: true,
      },
    },
  ],
});
router.beforeEach((to, from, next) => {
  setLoadingState(true); // 在导航开始时设置 loading 状态为 true

  // 模拟延迟，假设延迟 1000 毫秒
  setTimeout(() => {
    next(); // 继续导航
  }, 500);
});

router.afterEach(() => {
  setLoadingState(false); // 在导航完成后设置 loading 状态为 false
});

export default router;
