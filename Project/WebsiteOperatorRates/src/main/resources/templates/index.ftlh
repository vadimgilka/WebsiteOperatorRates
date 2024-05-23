<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Тарифы мобильных операторов</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="/static/css/styles.css">
</head>
<body>
<header class="header-section text-center">
    <div class="overlay">
        <h1 class="header-text">Выберите свой тариф!</h1>
    </div>
</header>
<main class="container mt-4">
    <form action="/" method="get">
        <div class="filter-section bg-light p-4 rounded mb-4">
            <div class="form-row">
                <div class="form-group col-md-3">
                    <div class="filter-group">
                        <label class="filter-label">Цена</label>
                        <input type="number" class="form-control" name="priceFrom" placeholder="От" value="${priceFrom}">
                        <input type="number" class="form-control mt-2" name="priceTo" placeholder="До" value="${priceTo}">
                    </div>
                </div>
                <div class="form-group col-md-3">
                    <div class="filter-group">
                        <label class="filter-label">Гигабайты</label>
                        <input type="number" class="form-control" name="gbFrom" placeholder="От" value="${gbFrom}">
                        <input type="number" class="form-control mt-2" name="gbTo" placeholder="До" value="${gbTo}">
                    </div>
                </div>
                <div class="form-group col-md-3">
                    <div class="filter-group">
                        <label class="filter-label">Минуты</label>
                        <input type="number" class="form-control" name="minutesFrom" placeholder="От" value="${minutesFrom}">
                        <input type="number" class="form-control mt-2" name="minutesTo" placeholder="До" value="${minutesTo}">
                    </div>
                </div>
                <div class="form-group col-md-3">
                    <div class="filter-group">
                        <label class="filter-label">СМС</label>
                        <input type="number" class="form-control" name="messagesFrom" placeholder="От" value="${messagesFrom}">
                        <input type="number" class="form-control mt-2" name="messagesTo" placeholder="До" value="${messagesTo}">
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="col text-center">
                    <button type="submit" class="btn btn-primary">Поиск</button>
                </div>
            </div>
        </div>
    </form>
    <table class="table table-striped table-bordered">
        <thead class="thead-light">
        <tr>
            <th>Оператор</th>
            <th>Тариф</th>
            <th>Гигабайты</th>
            <th>Минуты</th>
            <th>СМС</th>
            <th>Стоимость</th>
        </tr>
        </thead>
        <tbody>
        <#list rates as rate>
            <tr>
                <td>${rate.providerName}</td>
                <td><a href="${rate.link}" target="_blank">${rate.name}</a></td>
                <td>${rate.gigabytesOfInternet} ГБ</td>
                <td>${rate.minutesOfCall}</td>
                <td>${rate.messages}</td>
                <td>${rate.price} р/мес</td>
            </tr>
        </#list>
        </tbody>
    </table>
</main>
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.3/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>