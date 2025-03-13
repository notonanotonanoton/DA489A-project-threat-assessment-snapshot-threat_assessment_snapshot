import { getCookie } from "./cookieUtil";
import { clearCookie } from "./cookieUtil";

document.addEventListener('DOMContentLoaded', () => {
    const searchInput = document.getElementById('search-input') as HTMLInputElement;
    const searchIcon = document.getElementById('search-icon');
    const logo = document.querySelector('.logo') as HTMLDivElement;
    const menuIcon = document.querySelector('.menu-icon') as HTMLDivElement;
    const popupMenu = document.getElementById('popup-menu') as HTMLDivElement;
    const homeLink = document.getElementById('home-link') as HTMLDivElement;
    const settingsLink = document.getElementById('settings-link') as HTMLDivElement;
    const loginLogoutLink = document.getElementById('login-logout-link') as HTMLDivElement;


    const performSearch = (searchTerm: string) => {
        window.location.href = `../html/search-page.html?query=${encodeURIComponent(searchTerm)}`;
    }

    searchInput.addEventListener('keypress', (event) => {
        if(event.key === 'Enter') {
            const searchTerm = searchInput.value.trim();

            if(searchTerm){
                performSearch(searchTerm);
            }
        }
    });

    searchIcon?.addEventListener('click', () => {
        const searchTerm = searchInput.value.trim();

            if(searchTerm){
                performSearch(searchTerm);
            }
    });

    logo?.addEventListener('click', () => {
        const accessToken = getCookie('accessToken');

        if(accessToken){
            window.location.href = `../html/home-page.html`;
        }else {
            window.location.href = `../html/login-page.html`;
        }
    });

    menuIcon?.addEventListener('click', () => {
        popupMenu.classList.toggle('active');
    });

    homeLink?.addEventListener('click', () => {
        const accessToken = getCookie('accessToken');
        if(accessToken){
            window.location.href = '../html/home-page.html';
        }else {
            window.location.href = '../html/login-page.html';
        }
    });

    settingsLink?.addEventListener('click', () => {
        window.location.href = '../html/settings-page.html';
    });

    const updateLoginLogoutLink = () => {
        const accessToken = getCookie('accessToken');
        if (accessToken){
            loginLogoutLink.textContent = 'Sign out';
            loginLogoutLink.removeEventListener('click', handleLogin);
            loginLogoutLink.addEventListener('click', handleLogout);
        } else {
            loginLogoutLink.textContent = 'Sign in';
            loginLogoutLink.removeEventListener('click', handleLogout);
            loginLogoutLink.addEventListener('click', handleLogin);
        }
    };

    const handleLogin = () => {
        window.location.href = '../html/login-page.html';
    };

    const handleLogout = () => {
        clearCookie('accessToken');
        window.location.href = '../html/login-page.html'; 
        //TODO maybe adjust so that server also drops token, logout endpoint?
    };

    updateLoginLogoutLink();
    menuIcon.addEventListener('click', updateLoginLogoutLink);

    document.addEventListener('click', (event) => {
        const target = event.target as HTMLElement;
        if(!popupMenu.contains(target) && !menuIcon.contains(target)){
            popupMenu.classList.remove('active');
        }
    });
});