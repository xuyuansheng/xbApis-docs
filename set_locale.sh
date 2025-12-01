#!/usr/bin/env bash
# ============================================
# Arch Linux 中文语言环境与字体精简配置脚本
# 作者: GPT-5
# 功能:
#   - 启用 zh_CN.UTF-8 locale
#   - 安装最基本的中文字体 (Noto Sans CJK)
#
# 参数说明:
#   --noconfirm : pacman 自动确认参数。
#                 在执行安装或更新时，不再询问 [Y/n]，
#                 适合自动化脚本或无人值守环境。
# ============================================

set -e  # 一旦出错立即退出脚本

echo "🧩 开始配置中文语言环境与基础字体..."

# 1. 检查 root 权限
if [[ $EUID -ne 0 ]]; then
   echo "❌ 请使用 root 权限运行此脚本：sudo ./setup_chinese_minimal.sh"
   exit 1
fi

# 2. 更新系统软件包数据库
echo "🔄 更新软件包数据库..."
pacman -Sy --noconfirm
# --noconfirm 用于自动确认，不再提示 "Proceed with installation? [Y/n]"

# 3. 安装最小字体包
echo "🈶 安装基础中文字体: Noto Sans CJK..."
pacman -S --noconfirm noto-fonts-cjk
# Noto Sans CJK 是 Google 提供的开源字体，覆盖简体中文、繁体中文、日文、韩文

# 4. 配置中文 Locale
echo "🌐 启用 zh_CN.UTF-8 语言环境..."
# 取消 locale.gen 文件中相应行的注释以启用
sed -i 's/^#zh_CN.UTF-8 UTF-8/zh_CN.UTF-8 UTF-8/' /etc/locale.gen
sed -i 's/^#en_US.UTF-8 UTF-8/en_US.UTF-8 UTF-8/' /etc/locale.gen

# 生成 locale 数据
locale-gen

# 设置系统默认语言
echo "LANG=zh_CN.UTF-8" > /etc/locale.conf
export LANG=zh_CN.UTF-8

# 5. 刷新字体缓存
echo "🧠 刷新字体缓存..."
fc-cache -fv > /dev/null

# 6. 完成
echo
echo "✅ 中文语言环境与字体已配置完成！"
echo "👉 当前语言: $(locale | grep LANG)"
echo "👉 字体: Noto Sans CJK 已安装"
echo "👉 建议执行 reboot 以完全生效。"
echo

exit 0
