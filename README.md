# Tlias Web Management

## 鉴权方案：双令牌模式

本项目采用 **Access Token + Refresh Token** 双令牌鉴权机制，优于传统的单令牌方案。

| 特性 | 单令牌 | 双令牌 |
|---|---|---|
| **Token 有效期** | 必须取折中值（通常几小时） | Access Token 短（15分钟），Refresh Token 长（7天） |
| **泄露风险** | 有效期长，泄露后攻击者窗口大 | Access Token 有效期极短，泄露影响小；Refresh Token 仅用于刷新和登出，不参与常规请求 |
| **刷新体验** | 过期后用户需重新登录 | 前端自动用 Refresh Token 换取新 Access Token，用户无感知 |
| **登出控制** | 服务端难以使 token 立即失效 | Refresh Token 可加入黑名单，立即失效 |

### 工作原理

1. **登录** → 服务端返回 `accessToken`（15分钟 JWT）+ `refreshToken`（7天随机字符串，SHA-256 哈希存入数据库）
2. **常规请求** → 请求头携带 `accessToken`，由过滤器验证
3. **自动刷新** → `accessToken` 过期后，前端用 `refreshToken` 调用 `/refresh` 接口获取新 `accessToken`
4. **登出** → 调用 `/logout` 接口将 `refreshToken` 加入黑名单，此后无法再用于刷新

### 接口

| 方法 | 路径 | 说明 |
|---|---|---|
| POST | `/login` | 登录，返回双令牌 |
| POST | `/refresh` | 刷新 Access Token |
| POST | `/logout` | 登出，禁用 Refresh Token |
