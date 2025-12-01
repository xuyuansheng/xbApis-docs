#!/bin/bash
# ⚠️ 警告：此脚本会清空磁盘，请确认 DISK 正确无误
DISK=/dev/sdX

echo "=== ⚠️ 警告：操作会清空磁盘 $DISK 上的所有数据 ==="
read -p "确认要继续吗？(yes/no): " CONFIRM
if [[ "$CONFIRM" != "yes" ]]; then
    echo "操作已取消"
    exit 1
fi

# ==== 步骤 1：显示磁盘信息 ====
echo "=== 步骤 1：显示磁盘信息 ==="
lsblk $DISK
sudo fdisk -l $DISK
read -p "确认磁盘正确？(yes/no): " CONFIRM
if [[ "$CONFIRM" != "yes" ]]; then
    echo "操作已取消"
    exit 1
fi

# ==== 步骤 2：创建 GPT 分区表 ====
echo "=== 步骤 2：创建 GPT 分区表 ==="
sudo parted --script $DISK mklabel gpt
echo "GPT 分区表创建完成"

# ==== 步骤 3：创建 EFI 分区 ====
echo "=== 步骤 3：创建 EFI 分区 (512MB) ==="
sudo parted --script $DISK mkpart ESP fat32 1MiB 513MiB
sudo parted --script $DISK set 1 boot on
echo "EFI 分区创建完成"
read -p "继续创建根分区？(yes/no): " CONFIRM
if [[ "$CONFIRM" != "yes" ]]; then
    echo "操作已取消"
    exit 1
fi

# ==== 步骤 4：创建根分区 ====
echo "=== 步骤 4：创建根分区 (剩余空间) ==="
sudo parted --script $DISK mkpart primary 513MiB 100%
echo "根分区创建完成"

# ==== 步骤 5：格式化分区 ====
echo "=== 步骤 5：格式化 EFI 分区为 FAT32 ==="
sudo mkfs.fat -F32 ${DISK}1
echo "EFI 分区格式化完成"

echo "=== 步骤 5：格式化根分区为 Btrfs ==="
sudo mkfs.btrfs -f ${DISK}2
echo "根分区格式化完成"

# ==== 步骤 6：挂载分区 ====
echo "=== 步骤 6：挂载根分区 ==="
sudo mount ${DISK}2 /mnt
echo "根分区已挂载到 /mnt"

echo "=== 步骤 6：挂载 EFI 分区 ==="
sudo mkdir -p /mnt/boot
sudo mount ${DISK}1 /mnt/boot
echo "EFI 分区已挂载到 /mnt/boot"

# ==== 步骤 7：创建 Btrfs 根子卷 ====
echo "=== 步骤 7：创建 Btrfs 根子卷 @ ==="
sudo btrfs subvolume create /mnt/@
echo "Btrfs 根子卷 @ 创建完成"

# ==== 步骤 8：重新挂载根子卷 ====
echo "=== 步骤 8：重新挂载根子卷 @ ==="
sudo umount /mnt
sudo mount -o subvol=@ ${DISK}2 /mnt
sudo mkdir -p /mnt/boot
sudo mount ${DISK}1 /mnt/boot
echo "根子卷 @ 已挂载到 /mnt，EFI 分区挂载到 /mnt/boot"

echo "=== ✅ 分区与挂载完成 ==="
