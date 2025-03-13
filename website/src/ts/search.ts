interface Plant {
    id: number;
    common_name: string | null;
    scientific_name: string | null;
    family: string | null;
    image_url: string | null;
    maintenance: string | null;
    light: string | null;
    watering_frequency: number | null;
    poisonous_to_pets: boolean | null;
    nickname?: string;
}

document.addEventListener('DOMContentLoaded', () => {
    const URLparams = new URLSearchParams(window.location.search);
    const searchTerm = URLparams.get('query');

    if(searchTerm){
        const searchQueryElement = document.getElementById('search-query');
        if(searchQueryElement){
            searchQueryElement.textContent = `"${searchTerm}"`;
        }
        fetchSearchResults(searchTerm);
    }
});

const fetchSearchResults = async (searchTerm: string) => {
    try {
        //const url = `/api/search/${encodeURIComponent(searchTerm)}`;
        const url = `http://localhost:7888/search/${encodeURIComponent(searchTerm)}`;

        console.log('This is the formatted url:', url);
        const response = await fetch(url);
        if(!response.ok){
            throw new Error('nätverket snear yao');
        }
        const plants: Plant[] = await response.json();
        console.log('Plants:', plants);
        updateSearchResults(plants);
    }catch(error) {
        console.error('Something went wrong with fetch: ', error);
    }
};

const updateSearchResults = (plants: Plant[]) => {
    const searchResultsContainer = document.querySelector('.search-results');

    if(searchResultsContainer){
        searchResultsContainer.innerHTML = '';

        plants.forEach(plant => {
            const searchHit = document.createElement('div');
            searchHit.className = 'search-hit';

            const plantImage = document.createElement('img');
            plantImage.src = plant.image_url || '../../public/plant-1573.png';
            plantImage.alt = 'Plant image';
            plantImage.className = 'plant-image';

            const plantDetails = document.createElement('div');
            plantDetails.className = 'plant-details';

            const plantName = document.createElement('h2');
            plantName.className = 'plant-name';
            plantName.textContent = plant.common_name || 'No known common name';

            const scientificName = document.createElement('p');
            scientificName.className = 'scientific-name';
            scientificName.textContent = plant.scientific_name || 'No known scientific name';

            const plantActions = document.createElement('div');
            plantActions.className = 'plant-actions';

            const infoButton = document.createElement('button');
            infoButton.className = 'info-button';
            infoButton.textContent = 'Plant info';

            infoButton.addEventListener('click', () => {
                window.location.href = `plant-info.html?id=${plant.id}`;
            });

            const addBtn = document.createElement('button');
            addBtn.className = 'library-button';
            addBtn.textContent = 'Add to garden';
            addBtn.addEventListener('click', () => {
                plantToAdd = plant;
                confirmAddModal.style.display = 'flex';
            });

            plantDetails.appendChild(plantName);
            plantDetails.appendChild(scientificName);

            plantActions.appendChild(infoButton);
            plantActions.appendChild(addBtn);

            searchHit.appendChild(plantImage);
            searchHit.appendChild(plantDetails);
            searchHit.appendChild(plantActions);

            searchResultsContainer.appendChild(searchHit);
            console.log('Plant data:', plant);

        });
    }
};
const addToGarden = (plant: Plant) => {
    const stored = localStorage.getItem('myGarden');
    let garden: Plant[] = stored ? JSON.parse(stored) : [];
    garden.push(plant);
    localStorage.setItem('myGarden', JSON.stringify(garden));
};

let plantToAdd: Plant | null = null;

const confirmAddModal = document.getElementById('confirmAddModal') as HTMLElement;
const yesAddButton = confirmAddModal.querySelector('.yes-add-button') as HTMLButtonElement;
const noAddButton = confirmAddModal.querySelector('.no-add-button') as HTMLButtonElement;

yesAddButton.addEventListener('click', () => {
    if (plantToAdd) {
        const nicknameInput = document.getElementById('plantNickname') as HTMLInputElement;
        let nickname = nicknameInput ? nicknameInput.value.trim() : "";
        if (nickname) {
            plantToAdd.nickname = nickname;
        } else {
            plantToAdd.nickname = "";
        }
        addToGarden(plantToAdd);
        const toastName = plantToAdd.nickname ? plantToAdd.nickname : plantToAdd.common_name || plantToAdd.scientific_name || 'Unknown';
        showToast(`Perfect, added "${toastName}" to the garden!`);
        plantToAdd = null;
    }
    confirmAddModal.style.display = 'none';
});

noAddButton.addEventListener('click', () => {
    plantToAdd = null;
    confirmAddModal.style.display = 'none';
});

// Function to show a toast notification
function showToast(message: string) {
    const toast = document.getElementById('toast') as HTMLElement;
    if (toast) {
        toast.textContent = message;
        toast.style.display = 'block';
        void toast.offsetWidth;
        toast.classList.add('show');
        setTimeout(() => {
            toast.classList.remove('show');
            setTimeout(() => {
                toast.style.display = 'none';
            }, 500);
        }, 3000);
    }
}
