# Critical Bugs to Fix

## 1. Security & Navigation
- [ ] CartController 类级别 @PreAuthorize("hasRole('USER')") 阻止管理员访问 - 需删除
- [ ] SecurityConfig 缺少 /cart/orderDetail/{id} 路径配置
- [ ] 添加 JSON API 端点用于购物车侧边栏（common.js 错误地解析HTML为JSON）

## 2. 购物车侧边栏修复（common.js loadCartSidebar 完全错误）
- [ ] 添加 /cart/viewJson 端点返回JSON
- [ ] 重写 common.js loadCartSidebar

## 3. 按钮样式检查
- [ ] 管理员登录按钮居中
- [ ] 用户登录按钮居中
- [ ] 注册按钮居中
- [ ] 修改密码按钮居中

## 4. 管理员订单数据问题
- [ ] AdminController orders/list 确保正确返回数据
- [ ] 修复管理员仪表盘销量图显示
- [ ] 统一所有后台页面分页功能

## 5. 清理冗余文件
- [ ] 删除 src/main/resources/static/ 目录下的所有静态HTML（已使用模板）
- [ ] 清理无用代码

## 6. 构建测试
- [ ] mvn clean package 构建