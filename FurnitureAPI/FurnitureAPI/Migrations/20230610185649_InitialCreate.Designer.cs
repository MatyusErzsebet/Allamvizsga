﻿// <auto-generated />
using System;
using FurnitureAPI.Data;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Infrastructure;
using Microsoft.EntityFrameworkCore.Metadata;
using Microsoft.EntityFrameworkCore.Migrations;
using Microsoft.EntityFrameworkCore.Storage.ValueConversion;

namespace FurnitureAPI.Migrations
{
    [DbContext(typeof(Context))]
    [Migration("20230610185649_InitialCreate")]
    partial class InitialCreate
    {
        protected override void BuildTargetModel(ModelBuilder modelBuilder)
        {
#pragma warning disable 612, 618
            modelBuilder
                .HasAnnotation("Relational:MaxIdentifierLength", 128)
                .HasAnnotation("ProductVersion", "5.0.17")
                .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

            modelBuilder.Entity("FurnitureAPI.Data.Models.DiscountEntity", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int")
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<DateTime?>("DeadLine")
                        .IsRequired()
                        .HasColumnType("datetime2");

                    b.Property<int>("FurnitureId")
                        .HasColumnType("int");

                    b.Property<int>("Percentage")
                        .HasColumnType("int");

                    b.HasKey("Id");

                    b.HasIndex("FurnitureId")
                        .IsUnique();

                    b.ToTable("Discounts");
                });

            modelBuilder.Entity("FurnitureAPI.Data.Models.FurnitureEntity", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int")
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<int>("AvailableQuantity")
                        .HasColumnType("int");

                    b.Property<string>("Description")
                        .IsRequired()
                        .HasMaxLength(10000)
                        .HasColumnType("nvarchar(max)");

                    b.Property<int>("FurnitureTypeId")
                        .HasColumnType("int");

                    b.Property<string>("ImageUrl")
                        .IsRequired()
                        .HasColumnType("nvarchar(max)");

                    b.Property<bool>("IsDeleted")
                        .HasColumnType("bit");

                    b.Property<string>("Name")
                        .IsRequired()
                        .HasMaxLength(100)
                        .HasColumnType("nvarchar(100)");

                    b.Property<float>("Price")
                        .HasColumnType("real");

                    b.Property<string>("Size")
                        .IsRequired()
                        .HasMaxLength(30)
                        .HasColumnType("nvarchar(30)");

                    b.HasKey("Id");

                    b.HasIndex("FurnitureTypeId");

                    b.ToTable("Furnitures");
                });

            modelBuilder.Entity("FurnitureAPI.Data.Models.FurnitureTypeEntity", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int")
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<string>("Name")
                        .IsRequired()
                        .HasColumnType("nvarchar(450)");

                    b.HasKey("Id");

                    b.HasIndex("Name")
                        .IsUnique();

                    b.ToTable("FurnitureTypes");
                });

            modelBuilder.Entity("FurnitureAPI.Data.Models.PurchaseEntity", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int")
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<string>("Address")
                        .IsRequired()
                        .HasMaxLength(300)
                        .HasColumnType("nvarchar(300)");

                    b.Property<DateTime?>("Date")
                        .IsRequired()
                        .HasColumnType("datetime2");

                    b.Property<int>("FurnitureId")
                        .HasColumnType("int");

                    b.Property<float>("Price")
                        .HasColumnType("real");

                    b.Property<int>("Quantity")
                        .HasColumnType("int");

                    b.Property<int>("UserId")
                        .HasColumnType("int");

                    b.HasKey("Id");

                    b.HasIndex("FurnitureId");

                    b.HasIndex("UserId");

                    b.ToTable("Purchases");
                });

            modelBuilder.Entity("FurnitureAPI.Data.Models.ReviewEntity", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int")
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<string>("Comment")
                        .HasMaxLength(500)
                        .HasColumnType("nvarchar(500)");

                    b.Property<DateTime?>("Date")
                        .IsRequired()
                        .HasColumnType("datetime2");

                    b.Property<int>("FurnitureId")
                        .HasColumnType("int");

                    b.Property<int>("Rating")
                        .HasColumnType("int");

                    b.Property<int>("UserId")
                        .HasColumnType("int");

                    b.HasKey("Id");

                    b.HasIndex("FurnitureId");

                    b.HasIndex("UserId");

                    b.ToTable("Reviews");
                });

            modelBuilder.Entity("FurnitureAPI.Data.Models.UnconfirmedUserEntity", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int")
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<DateTime?>("BirthDate")
                        .IsRequired()
                        .HasColumnType("datetime2");

                    b.Property<string>("Email")
                        .IsRequired()
                        .HasColumnType("nvarchar(450)");

                    b.Property<string>("FirstName")
                        .IsRequired()
                        .HasColumnType("nvarchar(max)");

                    b.Property<string>("LastName")
                        .IsRequired()
                        .HasColumnType("nvarchar(max)");

                    b.Property<string>("Mobile")
                        .IsRequired()
                        .HasColumnType("nvarchar(max)");

                    b.Property<string>("Password")
                        .IsRequired()
                        .HasColumnType("nvarchar(max)");

                    b.Property<string>("Role")
                        .IsRequired()
                        .HasColumnType("nvarchar(max)");

                    b.Property<string>("ValidationKey")
                        .IsRequired()
                        .HasMaxLength(100)
                        .HasColumnType("nvarchar(100)");

                    b.Property<DateTime?>("ValidationKeyExpirationDate")
                        .IsRequired()
                        .HasColumnType("datetime2");

                    b.HasKey("Id");

                    b.HasIndex("Email")
                        .IsUnique();

                    b.ToTable("UnconfirmedUsers");
                });

            modelBuilder.Entity("FurnitureAPI.Data.Models.UserEntity", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int")
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<DateTime?>("BirthDate")
                        .IsRequired()
                        .HasColumnType("datetime2");

                    b.Property<string>("Email")
                        .IsRequired()
                        .HasColumnType("nvarchar(450)");

                    b.Property<string>("FirstName")
                        .IsRequired()
                        .HasColumnType("nvarchar(max)");

                    b.Property<DateTime?>("InsertedDate")
                        .IsRequired()
                        .HasColumnType("datetime2");

                    b.Property<string>("LastName")
                        .IsRequired()
                        .HasColumnType("nvarchar(max)");

                    b.Property<string>("Mobile")
                        .IsRequired()
                        .HasColumnType("nvarchar(max)");

                    b.Property<string>("Password")
                        .IsRequired()
                        .HasColumnType("nvarchar(max)");

                    b.Property<string>("Role")
                        .IsRequired()
                        .HasColumnType("nvarchar(max)");

                    b.HasKey("Id");

                    b.HasIndex("Email")
                        .IsUnique();

                    b.ToTable("Users");
                });

            modelBuilder.Entity("FurnitureAPI.Data.Models.UserForgotPasswordKeyEntity", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int")
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<string>("ChangeKey")
                        .IsRequired()
                        .HasMaxLength(8)
                        .HasColumnType("nvarchar(8)");

                    b.Property<bool>("Isverified")
                        .HasColumnType("bit");

                    b.Property<DateTime?>("KeyExpirationDate")
                        .IsRequired()
                        .HasColumnType("datetime2");

                    b.Property<int>("UserId")
                        .HasColumnType("int");

                    b.HasKey("Id");

                    b.HasIndex("UserId")
                        .IsUnique();

                    b.ToTable("UserForgotPasswordKeys");
                });

            modelBuilder.Entity("FurnitureAPI.Data.Models.DiscountEntity", b =>
                {
                    b.HasOne("FurnitureAPI.Data.Models.FurnitureEntity", "Furniture")
                        .WithMany()
                        .HasForeignKey("FurnitureId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.Navigation("Furniture");
                });

            modelBuilder.Entity("FurnitureAPI.Data.Models.FurnitureEntity", b =>
                {
                    b.HasOne("FurnitureAPI.Data.Models.FurnitureTypeEntity", "FurnitureType")
                        .WithMany("Furnitures")
                        .HasForeignKey("FurnitureTypeId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.Navigation("FurnitureType");
                });

            modelBuilder.Entity("FurnitureAPI.Data.Models.PurchaseEntity", b =>
                {
                    b.HasOne("FurnitureAPI.Data.Models.FurnitureEntity", "Furniture")
                        .WithMany("Purchases")
                        .HasForeignKey("FurnitureId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.HasOne("FurnitureAPI.Data.Models.UserEntity", "User")
                        .WithMany("Purchases")
                        .HasForeignKey("UserId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.Navigation("Furniture");

                    b.Navigation("User");
                });

            modelBuilder.Entity("FurnitureAPI.Data.Models.ReviewEntity", b =>
                {
                    b.HasOne("FurnitureAPI.Data.Models.FurnitureEntity", "Furniture")
                        .WithMany("Reviews")
                        .HasForeignKey("FurnitureId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.HasOne("FurnitureAPI.Data.Models.UserEntity", "User")
                        .WithMany("Reviews")
                        .HasForeignKey("UserId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.Navigation("Furniture");

                    b.Navigation("User");
                });

            modelBuilder.Entity("FurnitureAPI.Data.Models.UserForgotPasswordKeyEntity", b =>
                {
                    b.HasOne("FurnitureAPI.Data.Models.UserEntity", "User")
                        .WithMany("UserPasswordChangeKeys")
                        .HasForeignKey("UserId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.Navigation("User");
                });

            modelBuilder.Entity("FurnitureAPI.Data.Models.FurnitureEntity", b =>
                {
                    b.Navigation("Purchases");

                    b.Navigation("Reviews");
                });

            modelBuilder.Entity("FurnitureAPI.Data.Models.FurnitureTypeEntity", b =>
                {
                    b.Navigation("Furnitures");
                });

            modelBuilder.Entity("FurnitureAPI.Data.Models.UserEntity", b =>
                {
                    b.Navigation("Purchases");

                    b.Navigation("Reviews");

                    b.Navigation("UserPasswordChangeKeys");
                });
#pragma warning restore 612, 618
        }
    }
}
