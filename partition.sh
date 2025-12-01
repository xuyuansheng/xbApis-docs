#!/bin/bash

# ==== 设置 UTF-8 环境，保证中文提示显示正常 ====
export LANG=en_US.UTF-8
export LC_ALL=en_US.UTF-8

echo "=== ⚠️ 警告：此操作会清空磁盘上的所有数据 ==="

# ==== 步骤 0：列出可用磁盘，让用户选择 ====
echo "=== 可用磁盘列表 ==="
lsblk -d -o NAME,SIZE,MODEL | grep -v "loop"
echo
read -p "请输入要操作的磁盘（例如 sda，不要带分区号）: " DISK_NAME
DISK="/dev/$DISK_NAME"

echo
echo "你选择的磁盘是：$DISK"
lsblk $DISK
read -p "⚠️ 确认要清空该磁盘吗？(yes/no): " CONFIRM
if [[ "$CONFIRM" != "yes" ]]; then
    echo "操作已取消"
    exit 1
fi

# ==== 步骤 1：创建 GPT 分区表 ====
echo "=== 创建 GPT 分区表 ==="
sudo parted --script $DISK mklabel gpt
echo "GPT 分区表创建完成"

# ==== 步骤 2：创建 EFI 分区 (512MB) ====
echo "=== 创建 EFI 分区 (512MB) ==="
sudo parted --script $DISK mkpart ESP fat32 1MiB 513MiB
sudo parted --script $DISK set 1 boot on
echo "EFI 分区创建完成"

# ==== 步骤 3：创建根分区 (剩余空间) ====
echo "=== 创建根分区 (剩余空间) ==="
sudo parted --script $DISK mkpart primary 513MiB 100%
echo "根分区创建完成"

# ==== 步骤 4：格式化分区 ====
EFI_PART="${DISK}1"
ROOT_PART="${DISK}2"

echo "=== 格式化 EFI 分区为 FAT32 ==="
sudo mkfs.fat -F32 $EFI_PART
echo "EFI 分区格式化完成"

echo "=== 格式化根分区为 Btrfs ==="
sudo mkfs.btrfs -f $ROOT_PART
echo "根分区格式化完成"

# ==== 步骤 5：挂载分区 ====
echo "=== 挂载根分区 ==="
sudo mount $ROOT_PART /mnt
echo "根分区已挂载到 /mnt"

echo "=== 挂载 EFI 分区 ==="
sudo mkdir -p /mnt/boot
sudo mount $EFI_PART /mnt/boot
echo "EFI 分区已挂载到 /mnt/boot"

# ==== 步骤 6：创建 Btrfs 根子卷 @ ====
echo "=== 创建 Btrfs 根子卷 @ ==="
sudo btrfs subvolume create /mnt/@
echo "Btrfs 根子卷 @ 创建完成"

# ==== 步骤 7：重新挂载根子卷 @ ====
echo "=== 重新挂载根子卷 @ ==="
sudo umount /mnt
sudo mount -o subvol=@ $ROOT_PART /mnt
sudo mkdir -p /mnt/boot
sudo mount $EFI_PART /mnt/boot
echo "根子卷 @ 已挂载到 /mnt，EFI 分区挂载到 /mnt/boot"

echo
echo "=== ✅ 分区与挂载完成 ==="
echo "根子卷挂载在 /mnt"
echo "EFI 分区挂载在 /mnt/boot"
