import React, { useState, useEffect, useRef } from "react";
import { Link } from "react-router-dom";
import { motion, AnimatePresence } from "framer-motion";
import { GiHamburgerMenu } from "react-icons/gi";
import { VscClose } from "react-icons/vsc";
import useNavbarToggle from "../hooks/useNavbarToggle";

const NavbarPage = () => {
    const { isOpen, toggleMenu, menuRef } = useNavbarToggle();

    return (
        <motion.nav
            className="fixed top-0 left-0 w-full text-white z-50"
            initial={{ opacity: 0, y: -30 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5 }}
        >
            <nav className="max-w-8xl mx-auto px-12 py-8 flex items-center justify-between">
                <div className="text-xl font-bold tracking-tight">
                    <Link to="/" className="text-white hover:text-gray-300">
                        DEVSIGN
                    </Link>
                </div>

                {/* ✅ Desktop nav */}
                <ul className="hidden md:flex space-x-6 text-lg font-medium text-white items-cnenter">
                    <li><Link to="/about" className="hover:text-gray-300">About</Link></li>
                    <li><Link to="/board" className="hover:text-gray-300">Board</Link></li>
                    <li><Link to="/login" className="hover:text-gray-300">LogIn</Link></li>
                    <li><Link to="/signup" className="hover:text-gray-300">SignUp</Link></li>
                </ul>

                {/* ✅ Mobile nav */}
                <button onClick={toggleMenu} className="text-white md:hidden">
                    {isOpen ? (
                        <VscClose className="w-6 h-6" strokeWidth={1} />
                    ) : (
                        <GiHamburgerMenu className="w-6 h-6" />
                    )}
                </button>
            </nav>

            {isOpen && (
                <div className="md:hidden px-12 pb-6" ref={menuRef}>
                    <ul className="flex flex-col space-y-3 text-white text-sm font-bold">
                        <li><Link to="/about" onClick={toggleMenu} className="hover:text-gray-300">About</Link></li>
                        <li><Link to="/board" onClick={toggleMenu} className="hover:text-gray-300">Board</Link></li>
                        <li><Link to="/login" onClick={toggleMenu} className="hover:text-gray-300">LogIn</Link></li>
                        <li><Link to="/signup" onClick={toggleMenu} className="hover:text-gray-300">SignUp</Link></li>
                    </ul>
                </div>
            )}
        </motion.nav>
    );
};

export default NavbarPage;
