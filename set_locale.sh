#!/usr/bin/env bash
# ============================================
# Arch Linux 中文语言环境与字体自动配置脚本
# 作者: GPT-5
# ============================================

set -e

echo "🧩 开始配置中文语言环境与字体..."

# 1. 检查 root 权限
if [[ $EUID -ne 0 ]]; then
   echo "❌ 请使用 root 权限运行此脚本：sudo ./setup_chinese_locale_fonts.sh"
   exit 1
fi

# 2. 更新系统
echo "🔄 更新系统..."
pacman -Syu --noconfirm

# 3. 安装中文字体
echo "🈶 安装中文字体..."
pacman -S --noconfirm noto-fonts-cjk wqy-zenhei wqy-microhei adobe-source-han-sans-cn-fonts

# 4. 配置中文 Locale
echo "🌐 配置 zh_CN.UTF-8 语言环境..."
# 取消 locale.gen 文件中注释
sed -i 's/^#zh_CN.UTF-8 UTF-8/zh_CN.UTF-8 UTF-8/' /etc/locale.gen
sed -i 's/^#en_US.UTF-8 UTF-8/en_US.UTF-8 UTF-8/' /etc/locale.gen

# 生成 locale
locale-gen

# 设置系统默认语言
echo "LANG=zh_CN.UTF-8" > /etc/locale.conf
export LANG=zh_CN.UTF-8

# 5. 刷新字体缓存
echo "🧠 刷新字体缓存..."
fc-cache -fv

# 6. 完成提示
echo
echo "✅ 中文环境与字体已配置完成！"
echo "👉 当前语言: $(locale | grep LANG)"
echo "👉 建议重启系统以使更改完全生效。"
echo

exit 0
